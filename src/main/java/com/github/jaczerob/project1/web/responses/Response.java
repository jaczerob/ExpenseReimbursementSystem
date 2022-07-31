package com.github.jaczerob.project1.web.responses;

/**
 * An abstract base class for all Response objects
 * @author Jacob
 * @since 0.9
 * @since 0.9
 */
public abstract class Response {
    private String message;

    public String getMessage() {
        return this.message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }
}
