package com.zongkx.proxy;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.duckdb.DuckDBDriver;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@Configuration
@AutoConfigureAfter(value = DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
@Data
public class DuckDBConfiguration {
    private final DataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @Primary  // 标记为主数据源
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        return new HikariDataSource(config);
    }

    @Bean("duckdbDataSource")
    public DataSource duckdbDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DuckDBDriver.class.getCanonicalName());
        config.setMaximumPoolSize(10);
        config.setJdbcUrl("jdbc:duckdb:");
    //    config.setCatalog("");
    //    config.setSchema("");
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate duckdbJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(duckdbDataSource());
        jdbcTemplate.execute("PRAGMA extension_directory='" + path() + "';");
        jdbcTemplate.execute("install mysql_scanner;");
        jdbcTemplate.execute("load mysql_scanner;");
        DataBase dataBase = new DataBase(dataSourceProperties);
        if(StringUtils.hasLength(dataBase.getPort())){
            String s = "ATTACH 'host=%s password=%s user=%s port=%s database=%s' AS mysql (TYPE mysql)";
            jdbcTemplate.execute(String.format(s, dataBase.getIp(), dataSourceProperties.getPassword(),
                dataSourceProperties.getUsername(), dataBase.getPort(), dataBase.getName()));
        }else{
            String s = "ATTACH 'host=%s password=%s user=%s  database=%s' AS mysql (TYPE mysql)";
            jdbcTemplate.execute(String.format(s, dataBase.getIp(), dataSourceProperties.getPassword(),
                dataSourceProperties.getUsername(), dataBase.getName()));
        }
        jdbcTemplate.execute("use mysql."+dataBase.getName());
        
        return jdbcTemplate;
    }

    @Bean("entityManagerFactory")
    public EntityManagerFactory createEmf() {
        final Map<String, EntityManagerFactory> entityManagerFactoryMap = new HashMap<>();
        final EntityManagerFactory defaultEmf =
                this.createEntityManagerFactory(MySQLDialect.class.getName(), "mysqlFactory", dataSource()
                );
        entityManagerFactoryMap.put("duckdb",
                this.createEntityManagerFactory(DuckDBDialect.class.getName(), "duckdbFactory", duckdbDataSource()
                ));
        entityManagerFactoryMap.put("default", defaultEmf);
        final int hashcode = UUID.randomUUID().toString().hashCode();
        final Object emf = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{EntityManagerFactory.class},
                (proxy, method, args) -> {
                    if ("hashCode".equals(method.getName())) {
                        return hashcode;
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    String routingDataSource1 = DuckDBThreadLocal.getRoutingDataSource();
                    Object instance1 = entityManagerFactoryMap.get(routingDataSource1);
                    if (Objects.nonNull(instance1)) {
                        return method.invoke(instance1);
                    }
                    return method.invoke(defaultEmf);
                }
        );
        return (EntityManagerFactory) emf;
    }

    @Bean("duckDBInterceptor")
    public WebMvcConfigurer duckDBInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                InterceptorRegistration addInterceptor = registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                        if (handler instanceof HandlerMethod handlerMethod) {
                            Method method = handlerMethod.getMethod();
                            if (method.isAnnotationPresent(DuckDBDataSource.class)) {
                                DuckDBThreadLocal.setRoutingDataSource("duckdb");
                            }
                        }
                        return true;
                    }

                    @Override
                    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                        DuckDBThreadLocal.removeRoutingDataSource();
                    }
                });
                addInterceptor.addPathPatterns("/**");
            }
        };
    }

    private EntityManagerFactory createEntityManagerFactory(String dialectClassName, String providerName, DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        if (Objects.equals(MySQLDialect.class.getName(), dialectClassName)) {
            Map<String, String> properties = jpaProperties.getProperties();
            properties.put("hibernate.ddl-auto", hibernateProperties.getDdlAuto());
            properties.put("hibernate.session_factory.statement_inspector", DuckDBInterceptor.class.getName());
            factory.setJpaPropertyMap(properties);
            factory.setPackagesToScan("com.*");
            factory.setDataSource(dataSource);
            final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setShowSql(jpaProperties.isShowSql());
            vendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setDatabase(Database.MYSQL);
            factory.setJpaVendorAdapter(vendorAdapter);
        } else {
            final Properties properties = new Properties() {{
                put("hibernate.implicit_naming_strategy", new SpringImplicitNamingStrategy());
                put("hibernate.session_factory.statement_inspector", DuckDBInterceptor.class.getName());
                put("hibernate.temp.use_jdbc_metadata_defaults", false);
            }};
            factory.setJpaProperties(properties);
            factory.setPackagesToScan("com.*");
            factory.setDataSource(dataSource);
            final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setShowSql(jpaProperties.isShowSql());
            vendorAdapter.setPrepareConnection(false);
            vendorAdapter.setDatabasePlatform(dialectClassName);
            factory.setJpaVendorAdapter(vendorAdapter);
        }
        factory.afterPropertiesSet();
        factory.setPersistenceUnitName(providerName + "Unit");
        factory.setBeanName(providerName + "Bean");
        return factory.getObject();
    }

    @SneakyThrows
    public String path() {
        String os = System.getProperty("os.name").toLowerCase();
        String scanPath, basePath, copyPath;
        if (os.contains("win")) {
            scanPath = "classpath*:/duckdb/extensions/v0.10.0/windows_amd64/mysql_scanner.duckdb_extension";
            basePath = "C:\\duckdb";
            copyPath = "/v0.10.0/windows_amd64/mysql_scanner.duckdb_extension";
        } else {
            scanPath = "classpath*:/duckdb/extensions/v0.10.0/linux_amd64_gcc4/mysql_scanner.duckdb_extension";
            basePath = System.getProperty("user.home");
            copyPath = "/v0.10.0/linux_amd64_gcc4/mysql_scanner.duckdb_extension";
        }
        Resource[] array = Stream.of(Optional.of(scanPath)
                        .orElse(Arrays.toString(new String[0])))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
        for (Resource resource : array) {
            Path outputPath = Paths.get(basePath + copyPath).normalize();
            Files.createDirectories(outputPath.getParent());
            Files.copy(resource.getInputStream(), outputPath, REPLACE_EXISTING);

        }
        return basePath;
    }

    private Resource[] getResources(String location) {
        final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}