package com.example.controller.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendService {


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    static final String topic = "test-topic";

    public void send(String message) {

        for (int i = 0; i < 50; i++) {
            kafkaTemplate.send(topic, message+"_"+i);
        }

    }

}
