package com.imran.amqp.creator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestCaseResponse {
        private String id;
        private String project;
        private String projectId;
        private String projectName;
        private String runId;
        private String createdBy;
        private String issueTrackerHost;
        private String issueId;
        private boolean delete = false;
        private String issueTrackerType;
        private String username;
        private String password;
        private String result;
        private boolean isClose = false;
        private String category;
        private String method;
        private String path;
        private String cvssScore;
        private String severity;
        private String issueTrackerAssigneeAccountId;
        private String message;


}
