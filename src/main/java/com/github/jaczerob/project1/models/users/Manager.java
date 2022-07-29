package com.github.jaczerob.project1.models.users;

/**
 * Represents a manager
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public class Manager extends User {
    /**
     * Creates an instance of {@link Manager}
     * @param id The ID of the manager
     * @param email The email of the manager
     * @param username The username of the user
     * @param password The password of the manager
     */
    public Manager(int id, String email, String username, String password) {
        super(id, email, username, password);
    }
}
