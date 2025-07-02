package com.example.demokafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    // 监听指定主题的消息
    @KafkaListener(topics = "test-topic",groupId = "demo-group")
    public void listen(String message) {
        System.out.printf("receive msg"+message);
    }
}