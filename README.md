<div align="center">
    <h1>Project 1: Expense Reimbursement System</h1>
    <h2>User Stories</h2>
</div>

## Common
### How to access the API
The base URL for this project is http://localhost:8080/. To access logging in, for example, you will use http://localhost:8080/login.

### Login/Logout
The way this is implemented is by making a POST request to `/login` with your username and password will check these fields against Users in the database and if there is a match, there will be a User (Employee or Manager depending on access) object attributed to the UserSession inside of Tomcat. This User object will be used to check access to the other endpoints in this program. When a `POST` request to `/logout` is called, the UserSession will be invalidated.

- `/login` API endpoint
    - [LoginServlet](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/web/servlets/common/LoginServlet.java)
    - [LoginServletTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/web/servlets/common/LoginServletTest.java)
- `/logout` API endpoint
    - Not implemented

## Employees
### Reimbursement Requests
Employees will be able to submit reimbursement requests by making a POST request to `/requests/submit` with the amount and type of the request. This will be submitted to the reimbursement request database. Employees can view their pending or resolved reimbursement requests by making GET requests to `/requests/pending` or `/requests/resolved` respectively.

- `/requests/submit` API endpoint
    - Not implemented
- `/requests/pending` API endpoint
    - Not implemented
- `/requests/resolved` API endpoint
    - Not implemented
<a name="reimbursement-management-system"></a>
- Reimbursement request database system
    - JDBC implementation
        - [ReimbursementRequestRepository](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/repositories/ReimbursementRequestRepository.java)
        - [ReimbursementRequestRepositoryTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/repositories/ReimbursementRequestRepositoryTest.java)
        - [PostgreSQL script](https://github.com/jaczerob/project1/blob/40f9bf53ff84bd2b45dff0a8fe7914f7fedaa8e4/src/main/resources/Script.psql#L10)
    - Service
        - [ReimbursementRequestService](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/services/ReimbursementRequestService.java)
        - [ReimbursementRequestServiceTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/services/ReimbursementRequestServiceTest.java)
    - Models
        - [ReimbursementRequest](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/models/requests/ReimbursementRequest.java)
        - [PendingReimbursementRequest](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/models/requests/PendingReimbursementRequest.java)
        - [ResolvedReimbursementRequest](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/models/requests/ResolvedReimbursementRequest.java)

### View/Update Information
Employees will be able to view their information by making a GET request to `/info`. This will show your email address, username, and employee ID. Employees will be able to update their information by making a PUT request to `/info/update` with the information you wish to update.

- `/info` API endpoint
    - [InfoServlet](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/web/servlets/common/InfoServlet.java)
    - [InfoServletTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/web/servlets/common/InfoServletTest.java)
- `/info/update` API endpoint
    - Not implemented
<a name="employee-management-system"></a>
- Employee management system
    - JDBC implementation
        - [UserRepository](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/repositories/UserRepository.java)
        - [UserRepositoryTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/repositories/UserRepositoryTest.java)
        - [PostgreSQL script](https://github.com/jaczerob/project1/blob/40f9bf53ff84bd2b45dff0a8fe7914f7fedaa8e4/src/main/resources/Script.psql)
    - Service
        - [UserService](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/services/UserService.java)
        - [UserServiceTest](https://github.com/jaczerob/project1/blob/master/src/test/java/com/github/jaczerob/services/UserServiceTest.java)
    - Models
        - [User](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/models/requests/User.java)
        - [Employee](https://github.com/jaczerob/project1/blob/master/src/main/java/com/github/jaczerob/project1/models/requests/Employee.java)

## Managers
### Reimbursement Requests
Managers will be able to approve or deny a pending reimbursement request by making a POST request to `/requests/approve/{id}` or `/requests/deny/{id}` respectively. To view an employee's requests, make a GET request to `/requests/{employee_id}`. To view all pending or resolved requests, make a GET request to `/requests/pending` or `/requests/resolved` respectively.

- `/requests/approve/{id}` API endpoint
    - Not implemented
- `/requests/deny/{id}` API endpoint
    - Not implemented
- `/requests/{employee_id}` API endpoint
    - Not implemented
- `/requests/pending` API endpoint
    - Not implemented
- `/requests/resolved` API endpoint
    - Not implemented
- Reimbursement request database system
    - See [Reimbursement Management System](#reimbursement-management-system)

### Employees
Managers can view all employees by making a GET request to `/employees`.

- `/employees` API endpoint
    - Not implemented
- Employee management system
    - See [Employee Management System](#employee-management-system)

<br>
<h2 align="center">How to run</h2>

## My VSCode Extensions
- Extension Pack for Java
    - Various extensions for easier Java development
- Thunder Client
    - Testing API endpoints
- SQLTools and SQLTools PostgreSQL/Redshift Driver
    - PostgreSQL database management
- Coverage Gutters
    - Unit test coverage per file

## How to build and deploy
- Requirements
    - Maven
    - Docker
    - docker-compose
- Run `mvn clean jacoco:prepare-agent install jacoco:report`
- Run `docker-compose -f docker-compose.*.yaml up -d --build`
    - *stack/aws depending on if you want to use PostgreSQL locally or via AWS

## Testing
- Test API endpoints with the Thunder Client extension
- Configure/manage database with the SQLTools extension