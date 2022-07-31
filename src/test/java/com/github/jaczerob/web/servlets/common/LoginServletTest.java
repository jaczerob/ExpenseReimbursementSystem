package com.github.jaczerob.web.servlets.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.servlets.common.LoginServlet;

public class LoginServletTest {
    LoginServlet loginServlet;
    ServletConfig config;
    ServletContext context;
    HttpServletRequest req;
    HttpServletResponse resp;
    HttpSession session;
    UserService userService;
    
    int gotStatus;
    HashMap<String, User> attributes;
    StringWriter sw;
    PrintWriter pw;
    ObjectMapper mapper;

    @Before
    public void init() throws IOException, ServletException {
        loginServlet = Mockito.spy(new LoginServlet());
        userService = Mockito.mock(UserService.class);
        config = Mockito.mock(ServletConfig.class);
        context = Mockito.mock(ServletContext.class);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        
        gotStatus = -1;
        attributes = new HashMap<>();
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        mapper = new ObjectMapper();

        Mockito.doReturn(config).when(loginServlet).getServletConfig();
        Mockito.doReturn(context).when(loginServlet).getServletContext();
        Mockito.when(context.getAttribute("userService")).thenReturn(userService);
        Mockito.when(context.getAttribute("objectMapper")).thenReturn(mapper);
        Mockito.when(resp.getWriter()).thenReturn(pw);
        Mockito.when(req.getSession(true)).thenReturn(session);
        
        loginServlet.init();
    }

    @Test
    public void testLoginSuccess() throws IOException, ServletException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_OK;

        Mockito.when(req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(session.getAttribute("user")).thenReturn(null);
        Mockito.when(userService.loginUser(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(wantStatus);

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String attributeName = (String) args[0];
                User gotUser = (User) args[1];
                attributes.put(attributeName, gotUser);
                return null;
            }
        }).when(session).setAttribute(Mockito.matches("user"), Mockito.isA(User.class));

        loginServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are successfully logged in!", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
        Assert.assertEquals(user, attributes.get("user"));
    }

    @Test
    public void testLoginFail_whenNoParametersSet() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn(null);
        Mockito.when(req.getParameter("password")).thenReturn(null);

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(wantStatus);

        loginServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("The parameters for this endpoint are: username, password", response.get("parameters"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testLoginFail_whenAlreadyLoggedIn() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn("");
        Mockito.when(req.getParameter("password")).thenReturn("");
        Mockito.when(session.getAttribute("user")).thenReturn("");

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(wantStatus);

        loginServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are already logged in!", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testLoginFail_whenInvalidDetails() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(session.getAttribute("user")).thenReturn(null);
        Mockito.when(userService.loginUser(user.getUsername(), user.getPassword())).thenReturn(Optional.empty());
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(wantStatus);

        loginServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or password is invalid. Make sure your username is between 1 and 32 characters and your password is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testLoginFail_whenIllegalParameters() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn(user.getUsername());
        Mockito.when(req.getParameter("password")).thenReturn(user.getPassword());
        Mockito.when(session.getAttribute("user")).thenReturn(null);
        Mockito.when(userService.loginUser(user.getUsername(), user.getPassword())).thenThrow(IllegalArgumentException.class);
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(wantStatus);

        loginServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or password is invalid. Make sure your username is between 1 and 32 characters and your password is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }
}
