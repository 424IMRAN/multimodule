import requests

url = 'http://localhost:9000/send/'
myobj =     {

            "project":"demo",
            "projectId":"12345",
            "projectName":"Swish",
            "runId":"",
            "createdBy":"Imran",
            "issueTrackerHost":"localhost",
            "issueId":"issue_1",
            "delete":"false",
            "issueTrackerType":"id",
            "username":"",
            "password":"",
            "result":"Working",
            "isClose":"false",
            "category":"",
            "method":"",
            "path":"",
            "cvssScore":"",
            "severity":"",
            "issueTrackerAssigneeAccountId":"",
            "Message":"welcome"
        }


x = requests.post(url, data = myobj)

#print the response text (the content of the requested file):

print(x.text)