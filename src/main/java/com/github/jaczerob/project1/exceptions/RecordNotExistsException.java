package com.github.jaczerob.project1.exceptions;

/**
 * Represents an exception that has occurred if a record that does not
 * exist was attempted to be updated
 * @author Jacob
 * @since 0.1
 * @version 0.3
 */
public class RecordNotExistsException extends ERSException {
    /**
     * Constructor for class {@link RecordNotExistsException}
     */
    public RecordNotExistsException() {
        super("This record does not exist.");
    }
}