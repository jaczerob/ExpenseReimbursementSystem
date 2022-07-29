<div align="center">
    <h1>Project 1: Expense Reimbursement System</h1>
    <h2 align="center">User Stories</h2>
</div>

## Common
### Login/Logout
The way this is implemented is by making a POST request to `/login` with your username and password will check these fields against Users in the database and if there is a match, there will be a User (Employee or Manager depending on access) object attributed to the UserSession inside of Tomcat. This User object will be used to check access to the other endpoints in this program. When a `POST` request to `/logout` is called, the UserSession will be invalidated.

- `/login` API endpoint
    - Not implemented
- `/logout` API endpoint
    - Not implemented
- User authentication system
    - Not implemented
- UserSession User management
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
    - Not implemented
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
