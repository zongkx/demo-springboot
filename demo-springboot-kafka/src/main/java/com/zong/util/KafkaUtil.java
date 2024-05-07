package com.zong.util;

import com.zong.conf.KafkaConf;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author zongkxc
 */
@Component
public class KafkaUtil {
    private static AdminClient adminClient;
    private static KafkaTemplate<String,Object> kafkaTemplate;
    public KafkaUtil(KafkaTemplate<String,Object> kafkaTemplate1, KafkaConf kafkaConf) {
        kafkaTemplate = kafkaTemplate1;
        adminClient = AdminClient.create(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConf.getSpringKafkaBootstrapServers()));
    }
    /**
     * 新增topic，支持批量
     */
    public static void createTopic(Collection<NewTopic> newTopics) {
        adminClient.createTopics(newTopics);
    }

    /**
     * 删除topic，支持批量
     */
    public static void deleteTopic(Collection<String> topics) {
        adminClient.deleteTopics(topics);
    }


    /**
     * 获取指定topic的信息
     */
    public static String getTopicInfo(Collection<String> topics) {
        AtomicReference<String> info = new AtomicReference<>("");
        try {
            adminClient.describeTopics(topics).all().get().forEach((topic, description) -> {
                for (TopicPartitionInfo partition : description.partitions()) {
                    info.set(info + partition.toString() + "\n");
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return info.get();
    }

    /**
     * 获取全部topic
     */
    public static List<String> getAllTopic() {
        try {
            return adminClient.listTopics().listings().get().stream().map(TopicListing::name).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 往topic中发送消息
     */
    public static void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
