package com.zongkx.conf;

import lombok.extern.slf4j.Slf4j;
import org.casbin.adapter.JDBCAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class CasbinAdapterConfig {

    private final DataSource dataSource;

    @Autowired
    public CasbinAdapterConfig(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    @Bean
    public JDBCAdapter adapterConfig() throws Exception {
        return new JDBCAdapter(dataSource);
    }
}