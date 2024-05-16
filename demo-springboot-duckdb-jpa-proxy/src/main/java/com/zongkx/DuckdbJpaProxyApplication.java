package com.zongkx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class DuckdbJpaProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DuckdbJpaProxyApplication.class, args);
    }

}
