package com.imran.amqp.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class IssueCreatorSchedule {
    @Autowired
    private IssueCreator issueCreator;
    @Autowired
    private TestCaseResponse testCaseResponse;

    private IssueDescription issueDescription = new IssueDescription();

    @Async
    public void runTask(){

        try {
            issueCreator.process(testCaseResponse, issueDescription);
            Thread.sleep(30000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
