package com.example.controller;

import com.example.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    private TestServiceImpl testService;

    @GetMapping("/test")
    public String test() {
        return testService.test();
    }

}
