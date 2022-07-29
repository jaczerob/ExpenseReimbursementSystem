package com.github.jaczerob.project1.services;

import java.util.List;
import java.util.Optional;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.repositories.UserRepository;

/**
 * Represents the user service bridge to the repository
 * @author Jacob
 * @version 0.1
 * @since 0.2
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
