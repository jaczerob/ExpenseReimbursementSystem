package com.github.jaczerob.web.servlets.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.web.servlets.common.LoginServlet;
import com.github.jaczerob.utils.ServletTestHelper;

public class LoginServletTest extends ServletTestHelper {
    private HashMap<String, User> attributes;

    @Override
    protected void setServlet() {
        this.testServlet = Mockito.mock(LoginServlet.class, Mockito.CALLS_REAL_METHODS);
    }

    @Before
    public void init() throws IOException, ServletException {
        this.attributes = new HashMap<>();
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String attributeName = (String) args[0];
                User gotUser = (User) args[1];
                attributes.put(attributeName, gotUser);
                return null;
            }
        }).when(this.session).setAttribute(Mockito.matches("user"), Mockito.isA(User.class));
    }

    @Test
    public void testLoginSuccess() throws IOException, ServletException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_OK;

        Mockito.when(this.req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(this.req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(this.session.getAttribute("user")).thenReturn(null);
        Mockito.when(this.userService.loginUser(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are successfully logged in!", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
        Assert.assertEquals(user, this.attributes.get("user"));
    }

    @Test
    public void testLoginFail_whenNoParametersSet() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn(null);
        Mockito.when(this.req.getParameter("password")).thenReturn(null);

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("The parameters for this endpoint are: username, password", response.get("parameters"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testLoginFail_whenAlreadyLoggedIn() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn("");
        Mockito.when(this.req.getParameter("password")).thenReturn("");
        Mockito.when(this.session.getAttribute("user")).thenReturn("");

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are already logged in!", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testLoginFail_whenInvalidDetails() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(this.req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(this.session.getAttribute("user")).thenReturn(null);
        Mockito.when(this.userService.loginUser(user.getUsername(), user.getPassword())).thenReturn(Optional.empty());

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or password is invalid. Make sure your username is between 1 and 32 characters and your password is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testLoginFail_whenIllegalParameters() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(this.req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(this.session.getAttribute("user")).thenReturn(null);
        Mockito.when(this.userService.loginUser(user.getUsername(), user.getPassword())).thenThrow(IllegalArgumentException.class);

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or password is invalid. Make sure your username is between 1 and 32 characters and your password is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }
}
