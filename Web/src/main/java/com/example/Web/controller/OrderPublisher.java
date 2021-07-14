package com.example.Web.controller;

import com.example.Web.DTO.Order;
import com.example.Web.DTO.OrderStatus;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderPublisher {
    @Autowired
    private RabbitTemplate template;

    @Value("${app.exchange}")
    private String exchange;
    @Value("${app.key}")
    private String key;

    @PostMapping("/{restaurantName}")
    public String sender(@RequestBody Order order, @PathVariable String restaurantName){
        order.setOrderId(UUID.randomUUID().toString());
        OrderStatus orderStatus = new OrderStatus(order,"In Progress","Order Received by "+restaurantName);
        template.convertAndSend(exchange,key,orderStatus);
         return "Success";
    }

}
