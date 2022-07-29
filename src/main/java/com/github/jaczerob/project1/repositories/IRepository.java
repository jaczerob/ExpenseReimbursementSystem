package com.github.jaczerob.project1.repositories;

import java.sql.SQLException;
import java.util.Optional;

import com.github.jaczerob.project1.exceptions.ConnectionException;
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.ERSObject;

/**
 * Represents a repository interface
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public interface IRepository<T extends ERSObject> {
    /**
     * Gets an object from the repository by its ID
     * @param id The ID of the object
     * @return The optional object
     * @throws SQLException If there is an error in the SQL statement or database
     * @throws ConnectionException If there is an error in the connection to the database
     */
    Optional<T> get(int id) throws SQLException, ConnectionException;

    /**
     * Updates the object in the repository with its current field values
     * @param obj The object to update
     * @throws SQLException If there is an error in the SQL statement or database
     * @throws ConnectionException If there is an error in the connection to the database
     * @throws RecordNotExistsException If the object does not exist in the database
     */
    void update(T obj) throws SQLException, ConnectionException, RecordNotExistsException;

    /**
     * Inserts an object into a repository
     * @param obj The object to insert
     * @throws SQLException If there is an error in the SQL statement or database
     * @throws ConnectionException If there is an error in the connection to the database
     * @throws RecordAlreadyExistsException If the object to be inserted already exists in the database
     */
    void insert(T obj) throws SQLException, ConnectionException, RecordAlreadyExistsException;
}
