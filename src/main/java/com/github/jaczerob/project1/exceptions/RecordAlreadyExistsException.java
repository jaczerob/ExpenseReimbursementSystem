package com.github.jaczerob.project1.exceptions;

/**
 * Represents an exception that has occurred if record was attempted to be
 * inserted but it already existed
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public class RecordAlreadyExistsException extends ERSException {
    /**
     * Constructor for class {@link RecordAlreadyExistsException}
     */
    public RecordAlreadyExistsException() {
        super("This record already exists.");
    }
}