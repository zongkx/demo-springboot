package com.zongkx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author zongkxc
 * @Description
 * @create 2021/6/1  14:32
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CliApp {
    public static void main(String[] args) {
        SpringApplication.run(CliApp.class,args);
    }
}
