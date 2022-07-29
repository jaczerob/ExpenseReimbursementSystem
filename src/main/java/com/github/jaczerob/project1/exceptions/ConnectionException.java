package com.github.jaczerob.project1.exceptions;

/**
 * Represents an exception that has occurred if the database has connection issues
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public class ConnectionException extends ERSException {
    /**
     * Constructor for class {@link ConnectionException}
     */
    public ConnectionException() {
        super("Exception within database connection.");
    }
}