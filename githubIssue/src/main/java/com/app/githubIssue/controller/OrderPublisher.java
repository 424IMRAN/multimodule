package com.app.githubIssue.controller;


import com.app.githubIssue.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/send")
public class OrderPublisher {
    @Autowired
    private RabbitTemplate template;

    @Value("${app.exchange}")
    private String exchange;
    @Value("${app.key}")
    private String key;

    @PostMapping("/")
    public String sender(@RequestBody Message message){
        template.convertAndSend(exchange,key,message);
         return "Success";
    }

}
