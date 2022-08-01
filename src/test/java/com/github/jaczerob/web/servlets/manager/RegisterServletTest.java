package com.github.jaczerob.web.servlets.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.servlets.manager.RegisterServlet;

public class RegisterServletTest {
    RegisterServlet registerServlet;
    ServletConfig config;
    ServletContext context;
    HttpServletRequest req;
    HttpServletResponse resp;
    HttpSession session;
    UserService userService;
    MailService mailService;
    
    int gotStatus;
    StringWriter sw;
    PrintWriter pw;
    ObjectMapper mapper;

    @Before
    public void init() throws IOException, ServletException {
        registerServlet = Mockito.spy(new RegisterServlet());
        userService = Mockito.mock(UserService.class);
        mailService = Mockito.mock(MailService.class);
        config = Mockito.mock(ServletConfig.class);
        context = Mockito.mock(ServletContext.class);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        
        gotStatus = -1;
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        mapper = new ObjectMapper();

        Mockito.doReturn(config).when(registerServlet).getServletConfig();
        Mockito.doReturn(context).when(registerServlet).getServletContext();
        Mockito.when(context.getAttribute("userService")).thenReturn(userService);
        Mockito.when(context.getAttribute("mailService")).thenReturn(mailService);
        Mockito.when(context.getAttribute("objectMapper")).thenReturn(mapper);
        registerServlet.init();
        
        Mockito.when(resp.getWriter()).thenReturn(pw);

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(resp).setStatus(Mockito.anyInt());
    }

    @Test
    public void testRegisterSuccess() throws IllegalArgumentException, RecordAlreadyExistsException, ServletException, IOException, AddressException, MessagingException {
        int wantStatus = HttpServletResponse.SC_OK;
        
        Mockito.when(req.getParameter("email")).thenReturn("");
        Mockito.when(req.getParameter("username")).thenReturn("");
        Mockito.doNothing().when(userService).registerUser(Mockito.any());
        Mockito.doNothing().when(mailService).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());

        registerServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You have successfully registered!", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testRegisterFail_whenNoParametersSet() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn(null);
        Mockito.when(req.getParameter("email")).thenReturn(null);

        registerServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("The parameters for this endpoint are: username, email", response.get("parameters"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testRegisterFail_whenLoginDetailsExist() throws ServletException, IOException, IllegalArgumentException, RecordAlreadyExistsException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn("");
        Mockito.when(req.getParameter("email")).thenReturn("");
        Mockito.doThrow(RecordAlreadyExistsException.class).when(userService).registerUser(Mockito.any());

        registerServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or email already exist.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testRegisterFail_whenInvalidRegistrationDetailsResponse() throws IllegalArgumentException, RecordAlreadyExistsException, ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(req.getParameter("username")).thenReturn("");
        Mockito.when(req.getParameter("email")).thenReturn("");
        Mockito.doThrow(IllegalArgumentException.class).when(userService).registerUser(Mockito.any());

        registerServlet.doPost(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Registration details are invalid. Make sure your username is between 1 and 32 characters and your e-mail is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }
}
