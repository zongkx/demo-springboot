package com.zongkx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zongkxc
 */
@Data
@ConfigurationProperties(prefix = "my.auth")
public class MyAuthConf {
    private Boolean enable;
    private List<String> whiteList;
}
