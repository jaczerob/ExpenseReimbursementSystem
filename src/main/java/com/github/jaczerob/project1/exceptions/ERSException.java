package com.github.jaczerob.project1.exceptions;

/**
 * The base exception class for all ERS exceptions
 * @author Jacob
 * @since 0.1
 * @version 0.3
 */
public class ERSException extends Exception {
    /**
     * Constructor for class {@link ERSException}
     * @param reason The reason for the exception
     */
    public ERSException(String reason) {
        super(reason);
    }
}