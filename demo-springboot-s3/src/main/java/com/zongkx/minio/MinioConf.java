package com.zongkx.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConf implements InitializingBean {

    private String accessKey;
    private String accessSecret;
    private String endpoint;
    private Integer port;

    private MinioClient minioClient;

    public MinioClient getMinioClient() {
        return minioClient;
    }

    @Override
    public void afterPropertiesSet() {
        this.minioClient = new MinioClient.Builder()
                .credentials(accessKey,accessSecret)
                .endpoint(endpoint,port,false)
                .build();
    }
}
