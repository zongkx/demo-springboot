package com.zong.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zongkxc
 */

@Component
@Data
public class KafkaConf {
    @Value("${spring.kafka.bootstrap-servers}")
    private String springKafkaBootstrapServers;
}
