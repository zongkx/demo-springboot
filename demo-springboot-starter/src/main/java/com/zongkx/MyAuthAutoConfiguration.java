package com.zongkx;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({MyAuthConf.class})
@RequiredArgsConstructor
public class MyAuthAutoConfiguration {

    private final MyAuthConf myAuth;


    @Bean("myAuthFilter")
    @ConditionalOnProperty(prefix = "my.auth",value = "enable",havingValue = "true")
    public FilterRegistrationBean<MyAuthFilter> filterRegistrationBean(){
        System.out.println(myAuth.getEnable());
        MyAuthFilter myAuthFilter = new MyAuthFilter(myAuth.getWhiteList());
        FilterRegistrationBean<MyAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(myAuthFilter);
        return filterRegistrationBean;
    }
}
