package com.github.jaczerob.project1.models.users;

/**
 * Represents an employee
 * @author Jacob
 * @since 0.1
 * @version 0.3
 */
public class Employee extends User {
    /**
     * Creates an instance of {@link Employee}
     * @param id The ID of the employee
     * @param email The email of the employee
     * @param username The username of the user
     * @param password The password of the employee
     */
    public Employee(int id, String email, String username, String password) {
        super(id, email, username, password);
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
