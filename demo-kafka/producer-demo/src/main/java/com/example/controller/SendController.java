package com.example.controller;


import com.example.controller.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {


    @Autowired
    SendService sendService;

    @PostMapping("/send/{message}")
    public void sendMessage( @PathVariable("message") String message){

        sendService.send(message);
    }






}
