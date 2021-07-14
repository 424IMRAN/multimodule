package com.imran.amqp.creator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ITTaskResponse {
    private String projectId;
    private String projectName;
    private String runId;
    private String userId;
    private String logs;
    private String issueStatus;
    private boolean success;
    private String testCaseResponseId;
    private String issueId;
    private boolean delete;
    private String issueTrackerType;
    private String issueDescription;
}
