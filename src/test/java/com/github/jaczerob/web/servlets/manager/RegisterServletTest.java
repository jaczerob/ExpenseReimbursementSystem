package com.github.jaczerob.web.servlets.manager;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.web.servlets.manager.RegisterServlet;
import com.github.jaczerob.utils.ServletTestHelper;

public class RegisterServletTest extends ServletTestHelper {
    @Override
    protected void setServlet() {
        this.testServlet = Mockito.mock(RegisterServlet.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testRegisterSuccess() throws IllegalArgumentException, RecordAlreadyExistsException, ServletException, IOException, AddressException, MessagingException {
        int wantStatus = HttpServletResponse.SC_OK;
        
        Mockito.when(this.req.getParameter("email")).thenReturn("");
        Mockito.when(this.req.getParameter("username")).thenReturn("");
        Mockito.doNothing().when(userService).registerUser(Mockito.any());
        Mockito.doNothing().when(mailService).sendEmail(Mockito.any(), Mockito.any(), Mockito.any());

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You have successfully registered!", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testRegisterFail_whenNoParametersSet() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn(null);
        Mockito.when(this.req.getParameter("email")).thenReturn(null);
        
        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("The parameters for this endpoint are: username, email", response.get("parameters"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testRegisterFail_whenLoginDetailsExist() throws ServletException, IOException, IllegalArgumentException, RecordAlreadyExistsException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn("");
        Mockito.when(this.req.getParameter("email")).thenReturn("");
        Mockito.doThrow(RecordAlreadyExistsException.class).when(userService).registerUser(Mockito.any());

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Username or email already exist.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testRegisterFail_whenInvalidRegistrationDetailsResponse() throws IllegalArgumentException, RecordAlreadyExistsException, ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_BAD_REQUEST;

        Mockito.when(this.req.getParameter("username")).thenReturn("");
        Mockito.when(this.req.getParameter("email")).thenReturn("");
        Mockito.doThrow(IllegalArgumentException.class).when(userService).registerUser(Mockito.any());

        this.testServlet.doPost(this.req, this.resp);

        pw.flush();
        Map<String, String> response = objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Registration details are invalid. Make sure your username is between 1 and 32 characters and your e-mail is between 1 and 255 characters long.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }
}
