package com.example.demokafka.controller;


import com.example.demokafka.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {


    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/send")
    public void sendMessage()
    {
        kafkaProducerService.sendMessage("test-topic", "Hello, Kafka!");

    }


}
