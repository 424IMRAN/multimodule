package com.imran.amqp.creator;

public interface IssueTrackerService {
    public ITTaskResponse process(TestCaseResponse task, IssueDescription issueDescription);
    public ITCommentsTaskResponse processComments(ITTask task);
}
