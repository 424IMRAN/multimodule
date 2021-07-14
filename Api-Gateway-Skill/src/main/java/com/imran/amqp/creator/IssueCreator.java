package com.imran.amqp.creator;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




    @Component
    public class IssueCreator {

        public static final String COLON = "  :  ";
        public static final String LINE_SEPERATOR = "\n";
        public static final String THREE_SPACE = "   ";

        /**
         * Issue open state
         */
        public static final String STATE_OPEN = "open";

        /**
         * Issue closed state
         */
        public static final String STATE_CLOSED = "closed";

        private String imIssueTrackerBot;

        private String imIssueTrackerBotSecretKey;

        final Logger logger = LoggerFactory.getLogger(getClass());

        //@Autowired
        private IssueCreatorSchedule issueCreatorSchedule=new IssueCreatorSchedule();

        public ThreadLocal<StringBuilder> taskLogger = new ThreadLocal<>();

        @Async
        public ITTaskResponse process(TestCaseResponse task, IssueDescription issueDescription) {
            logger.info("In IT GitIssueTrackerService for project [{}]", task.getProject());

            ITTaskResponse response = new ITTaskResponse();
        response.setProjectId(task.getProjectId());
        response.setProjectName(task.getProject());
        response.setRunId(task.getRunId());
        response.setUserId(task.getCreatedBy());

            try {

                taskLogger.set(new StringBuilder());
                //TODO Create/Update bugs/issue in gitbub

                RepositoryId repositoryId = RepositoryId.createFromUrl("https://github.com/424IMRAN/testing/issues");

                if (repositoryId == null) {
                     response.setLogs(taskLogger.get().toString());
                    logger.info(response.toString());
                    return response;
                }

                Issue issue = buildIssue(task, issueDescription);


                //creates an issue remotely

                // IssueService issueService = getIssueService(task.getUsername(), task.getPassword());

                    try{
                            issue.setTitle("This is issue no ");
                        issue = createIssue(issue, repositoryId);

                    }
                catch(Exception e){
                    if(e.getMessage().contains("You have triggered an abuse detection"))
                        issue.setTitle("This is issue no ");
                        issueCreatorSchedule.runTask();
                }


            response.setSuccess(true);
            response.setLogs(taskLogger.get().toString());

            response.setTestCaseResponseId(task.getId());
            response.setIssueId(String.valueOf(issue.getNumber()));
            response.setRunId(task.getRunId());
            response.setDelete(task.isDelete());
            response.setIssueTrackerType(task.getIssueTrackerType());

                return response;

            } catch (RuntimeException ex) {
                logger.warn(ex.getLocalizedMessage(), ex);
                  response.setLogs(taskLogger.get().toString());
            } catch (Exception ex) {
                logger.warn(ex.getLocalizedMessage(), ex);
                taskLogger.get().append(ex.getLocalizedMessage()).append("\n");
            }

            return response;

        }

        //@Override
        public ITCommentsTaskResponse processComments(ITTask task) {
            return null;
        }

        private Issue editIssue(Issue issue, RepositoryId repositoryId, TestCaseResponse task, ITTaskResponse response, IssueDescription issueDescription) throws IOException {

            IssueService issueService = getIssueService(task.getUsername(), task.getPassword());

            int issueNumber = Integer.parseInt(task.getIssueId());

            Issue issue_ = issueService.getIssue(repositoryId, issueNumber);

            if (StringUtils.equals(task.getResult(), "pass")) {
                issue_.setState(STATE_CLOSED);
                response.setIssueStatus(STATE_CLOSED);
            } else {
                issue_.setState(STATE_OPEN);
                response.setIssueStatus(STATE_OPEN);
            }

            if (true) {
                response.setIssueStatus(STATE_CLOSED);
                issue_.setState(STATE_CLOSED);
                addComment(issueService, repositoryId, task, issueDescription);
            }
            issue = issueService.editIssue(repositoryId, issue_);

            if (true) {
                addComment(issueService, repositoryId, task, issueDescription);
            }

            return issue;
        }

        private Issue buildIssue(TestCaseResponse task, IssueDescription issueDescription) {
            Issue issue = new Issue();
            //Title required
            //vulnerability [category] : Method:Endpoint
            if (task.getRunId() == null){
                issue.setTitle("First");
            }
            else {
                issue.setTitle("First Check");
            }

            issue.setState(IssueService.STATE_OPEN);

            String body = "Creating a simple issue";

            issue.setBody(body);

            List<Label> newLabels = new ArrayList<>();
            //building labels
            Label projectName = new Label();
            Label apisec = new Label();
            Label cvss = new Label();
            Label severity = new Label();

            projectName.setName("Automation");
            apisec.setName("swish");
            cvss.setName("CVSS_3.1 " + "5.2");
            severity.setName("severe");

            newLabels.add(projectName);
            newLabels.add(apisec);
            newLabels.add(cvss);
            newLabels.add(severity);

            issue.setLabels(newLabels);

            if (task.getIssueTrackerAssigneeAccountId() != null && StringUtils.isNotEmpty(task.getIssueTrackerAssigneeAccountId())) {
                User user = getUserService("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", "task.getIssueTrackerAssigneeAccountId()");
                if (user != null)
                    issue.setAssignee(user);
            }

            return issue;
        }

        private String buildComment(TestCaseResponse task, IssueDescription issueDescription) {


            StringBuilder sb = new StringBuilder();

            if (org.apache.commons.lang3.StringUtils.containsAny(task.getMessage(), "manually closed")) {
                sb
                        .append("Message").append(COLON).append("<html><b>" + task.getMessage() + "</b></html>").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
            }
            sb.append(issueDescription.toString());

            String body = sb.toString();

            return body;
        }
        @Async
        private Issue createIssue(Issue issue, RepositoryId repositoryId) throws IOException {

            IssueService issueService = getIssueService("","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");
            issue = issueService.createIssue(repositoryId, issue);

            return issue;
        }

//    @Async
//
//    public CompletableFuture<Issue> createIssue(Issue issue, RepositoryId repositoryId) throws InterruptedException {
//
//        logger.info("Looking up " + user);
//
//        String url = String.format("https://api.github.com/users/%s", user);
//
//        User results = restTemplate.getForObject(url, User.class);
//
//        // Artificial delay of 1s for demonstration purposes
//
//        Thread.sleep(1000L);
//
//        return CompletableFuture.completedFuture(results); //IT IS completedFuture
//
//    }

        private Comment addComment(IssueService issueService, RepositoryId repositoryId, TestCaseResponse task, IssueDescription issueDescription) throws IOException {
            Comment comment = issueService.createComment(repositoryId, Integer.parseInt(task.getIssueId()), buildComment(task, issueDescription));
            return comment;
        }

        private IssueService getIssueService(String username, String password) {

            GitHubClient client = new GitHubClient();
            IssueService issueService = null;
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                client.setCredentials(username, password);
                issueService = new IssueService(client);
            }

            if (issueService == null && StringUtils.isEmpty(username) && StringUtils.isNotEmpty(password)) {
                issueService = new IssueService();
                issueService.getClient().setOAuth2Token(password);
            }

            if (issueService == null) {
                issueService = getIssueService(imIssueTrackerBot, imIssueTrackerBotSecretKey);
            }

            return issueService;
        }

        private User getUserService(String password, String assignee) {
            GitHubClient client = new GitHubClient();
            UserService userService = null;
            User user = null;
            //username & pass feature is depricated
    /*    if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            client.setCredentials(username, password);
            userService = new UserService(client);
            try {
                user = userService.getUser(assignee);
                return user;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
//        if (StringUtils.isNotEmpty(password)) {
//            userService = new UserService();
//            userService.getClient().setOAuth2Token(password);
//            try {
//                user = userService.getUser(assignee);
//                return user;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
            return user;
        }

    }




