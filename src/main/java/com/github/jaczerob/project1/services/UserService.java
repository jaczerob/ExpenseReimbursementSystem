package com.github.jaczerob.project1.services;

import java.util.List;
import java.util.Optional;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.repositories.UserRepository;

/**
 * Represents the user service bridge to the repository
 * @author Jacob
 * @since 0.2
 * @version 0.14
 */
public class UserService {
    private UserRepository userRepository;

    /**
     * Constructor for {@link UserService}
     * @param userRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets a user by their username
     * @param username The username of the user
     * @return The optional user object
     * @throws IllegalArgumentException If the username is not valid
     */
    public Optional<User> getUser(String username) throws IllegalArgumentException {
        if (username.isEmpty() || username.length() > 32) throw new IllegalArgumentException("Invalid username");
        return this.userRepository.get(username);
    }

    /**
     * Gets a user by their username
     * @param username The username of the user
     * @return The optional user object
     * @throws IllegalArgumentException If the username is not valid
     */
    public Optional<User> getUser(int id) throws IllegalArgumentException {
        if (id < 0) throw new IllegalArgumentException("Invalid id");
        return this.userRepository.get(id);
    }

    /**
     * Updates an existing user
     * @param user The user to update
     * @throws RecordNotExistsException If the user does not exist
     */
    public void updateUser(User user) throws RecordNotExistsException {
        if (
            user.getID() < 0 || 
            user.getUsername().isEmpty() || 
            user.getUsername().length() > 32 ||
            user.getPassword().isEmpty() ||
            user.getPassword().length() > 255 ||
            user.getEmail().isEmpty() ||
            user.getEmail().length() > 255
        ) throw new IllegalArgumentException("Invalid properties");

        this.userRepository.update(user);
    }

    /**
     * Gets a user if the username and password match a record in the database
     * @param username The username of the user
     * @param password The password of the user
     * @return The optional user object
     * @throws IllegalArgumentException If the username or password is not valid
     */
    public Optional<User> loginUser(String username, String password) throws IllegalArgumentException {
        if (username.isEmpty() || username.length() > 32) throw new IllegalArgumentException("Invalid login details");
        if (password.isEmpty() || password.length() > 255) throw new IllegalArgumentException("Invalid login details");
        
        Optional<User> user = this.userRepository.get(username);
        if (user.isPresent())
            user = user.get().getPassword().equals(password) ? user : Optional.empty();

        return user;
    }

    /**
     * Registers a user
     * @param user The user to register
     * @throws RecordAlreadyExistsException If the user already exists
     * @throws IllegalArgumentException If the user properties are invalid
     */
    public void registerUser(User user) throws IllegalArgumentException, RecordAlreadyExistsException {
        if (
            user.getID() < 0 || 
            user.getUsername().isEmpty() || 
            user.getUsername().length() > 32 ||
            user.getPassword().isEmpty() ||
            user.getPassword().length() > 255 ||
            user.getEmail().isEmpty() ||
            user.getEmail().length() > 255
        ) throw new IllegalArgumentException("Invalid properties");

        this.userRepository.insert(user);
    }

    /**
     * Gets all employees
     * @return A list of all employees
     */
    public List<Employee> getAllEmployees() {
        return this.userRepository.getAllEmployees();
    }
}
