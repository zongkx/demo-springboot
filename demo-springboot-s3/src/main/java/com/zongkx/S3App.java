package com.zongkx;

import com.zongkx.minio.MinioUtil;
import com.zongkx.minio.S3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author zongkxc
 */

@SpringBootApplication
@Slf4j
public class S3App {
    public static void main(String[] args) {
        SpringApplication.run(S3App.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner() throws S3Exception {
        MinioUtil.createBucketIfNotExists("test");
        return args -> {};
    }
}
