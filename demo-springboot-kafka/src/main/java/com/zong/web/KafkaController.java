package com.zong.web;

import com.google.common.collect.Lists;
import com.zong.util.KafkaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * kafka控制器
 *
 * @author 154594742@qq.com
 * @date 2021/3/2 15:01
 */

@RestController
@Api(tags = "Kafka控制器")
@Slf4j
public class KafkaController {


    /**
     * 新增topic (支持批量，这里就单个作为演示)
     *
     * @param topic topic
     * @return ResponseVo
     */
    @ApiOperation("新增topic")
    @PostMapping("kafka")
    public ResponseEntity<String> add(String topic) {
        NewTopic newTopic = new NewTopic(topic, 3, (short) 1);
        KafkaUtil.createTopic(Lists.newArrayList(newTopic));
        return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
    }

    /**
     * 查询topic信息 (支持批量，这里就单个作为演示)
     *
     * @param topic 自增主键
     * @return ResponseVo
     */
    @ApiOperation("查询topic信息")
    @GetMapping("kafka/{topic}")
    public ResponseEntity<String> getBytTopic(@PathVariable String topic) {
        return new ResponseEntity<>(KafkaUtil.getTopicInfo(Lists.newArrayList(topic)),HttpStatus.ACCEPTED);
    }

    /**
     * 删除topic (支持批量，这里就单个作为演示)
     * (注意：如果topic正在被监听会给人感觉删除不掉（但其实是删除掉后又会被创建）)
     *
     * @param topic topic
     * @return ResponseVo
     */
    @ApiOperation("删除topic")
    @DeleteMapping("kafka/{topic}")
    public ResponseEntity<?> delete(@PathVariable String topic) {
        KafkaUtil.deleteTopic(Lists.newArrayList(topic));
        return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
    }

    /**
     * 查询所有topic
     *
     * @return ResponseVo
     */
    @ApiOperation("查询所有topic")
    @GetMapping("kafka/allTopic")
    public ResponseEntity<List<String>> getAllTopic() {
        return new ResponseEntity<>(KafkaUtil.getAllTopic(), HttpStatus.ACCEPTED);
    }

    /**
     * 生产者往topic中发送消息demo
     *
     * @param topic
     * @param message
     * @return
     */
    @ApiOperation("往topic发送消息")
    @PostMapping("kafka/message")
    public ResponseEntity<?> sendMessage(String topic, String message) {
        KafkaUtil.sendMessage(topic, message);
        return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
    }

    /**
     * 消费者示例demo
     * <p>
     * 基于注解监听多个topic，消费topic中消息
     * （注意：如果监听的topic不存在则会自动创建）
     */
    @KafkaListener(topics = {"topic1", "topic2", "topic3"})
    public void consume(String message) {
        log.info("receive msg: " + message);
    }
}
