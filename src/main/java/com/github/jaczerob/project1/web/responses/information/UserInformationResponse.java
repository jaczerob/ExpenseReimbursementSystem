package com.github.jaczerob.project1.web.responses.information;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing user information
 * @author Jacob
 * @since 0.10
 * @version 0.10
 */
public class UserInformationResponse extends Response {
    private User user;

    /**
     * Constructs a new UserInformationResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public UserInformationResponse(HttpServletResponse resp, User user) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("Here is your user information!");
        this.user = user;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public int getID() {
        return this.user.getID();
    }
}
