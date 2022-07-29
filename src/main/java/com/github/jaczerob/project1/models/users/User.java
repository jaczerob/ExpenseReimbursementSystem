package com.github.jaczerob.project1.models.users;

import com.github.jaczerob.project1.models.ERSObject;

/**
 * Represents a user
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public abstract class User extends ERSObject {
    protected String email;
    protected String username;
    protected String password;

    /**
     * The constructor for {@link User} and its subclasses
     * @param id The ID of the user
     * @param email The email of the user
     * @param username The username of the user
     * @param password The password of the user
     */
    public User(int id, String email, String username, String password) {
        super(id);
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;

        User other = (User) obj;
        return this.getID() == other.getID();
    }
}