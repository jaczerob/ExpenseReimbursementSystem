package com.github.jaczerob.project1.web.responses.common;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if there was a server error
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class ServerError extends Response {
    /**
     * Constructs a new ServerError
     * Sets the status code to 500 (Internal Server Error)
     * @param resp The raw HTTP response
     */
    public ServerError(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        this.setMessage("Sorry, there was an internal server error. Please try again later.");
    }
}
