package com.github.jaczerob.project1.repositories;

import java.util.Optional;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.ERSObject;

/**
 * Represents a repository interface
 * @author Jacob
 * @since 0.1
 * @version 0.3
 */
public interface IRepository<T extends ERSObject, U> {
    /**
     * Gets an object from the repository by its ID
     * @param id The identifier of the object
     * @return The optional object
     */
    Optional<T> get(U id);

    /**
     * Updates the object in the repository with its current field values
     * @param obj The object to update
     * @throws RecordNotExistsException If the object does not exist in the database
     */
    void update(T obj) throws RecordNotExistsException;

    /**
     * Inserts an object into a repository
     * @param obj The object to insert
     * @throws RecordAlreadyExistsException If the object to be inserted already exists in the database
     */
    void insert(T obj) throws RecordAlreadyExistsException;
}
