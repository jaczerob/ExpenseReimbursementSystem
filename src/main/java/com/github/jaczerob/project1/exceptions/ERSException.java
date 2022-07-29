package com.github.jaczerob.project1.exceptions;

/**
 * The base exception class for all ERS exceptions
 * @author Jacob
 * @version 0.1
 * @since 0.1
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