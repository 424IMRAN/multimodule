package com.imran.amqp;

import com.imran.amqp.creator.ITTaskResponse;
import com.imran.amqp.creator.IssueCreator;
import com.imran.amqp.creator.IssueDescription;
import com.imran.amqp.creator.TestCaseResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class OrderConsumer {

    private static final String queue = "test1_key";
    @Autowired
    private IssueCreator issueCreator;
    private IssueDescription issueDescription = new IssueDescription();
//    @Autowired
//    private TestCaseResponse testCaseResponse;
//    @Scheduled(fixedRate = 1000)
    @RabbitListener(queues = queue)
    public void consumer(TestCaseResponse testCaseResponse){

        issueCreator.process(testCaseResponse, issueDescription);

        System.out.println(testCaseResponse);
    }
}
