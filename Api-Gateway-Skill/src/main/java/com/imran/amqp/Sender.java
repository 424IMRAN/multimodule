package com.imran.amqp;

import com.imran.amqp.creator.TestCaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Syed Jaleel
 */
@RestController
@RequestMapping("/send")
public class Sender {


    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate template;
    @Value("${app.exchange}")
    private String exchange;
    @Value("${app.key}")
    private String routingKey;


//    @Autowired
//    public Sender(AmqpTemplate template,
//                  @Value("${app.exchange}") String exchange,
//                  @Value("${app.key}") String routingKey) {
//
//    }

@PostMapping("/")
    public TestCaseResponse sendTask(@RequestBody TestCaseResponse task) {

        template.convertAndSend(exchange, routingKey, task);
        return task;
    }
}
