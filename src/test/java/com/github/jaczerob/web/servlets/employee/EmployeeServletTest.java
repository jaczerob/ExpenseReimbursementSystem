package com.github.jaczerob.web.servlets.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

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
import com.github.jaczerob.project1.web.servlets.employee.EmployeeServlet;

public class EmployeeServletTest {
    EmployeeServlet employeeServlet;
    HttpServletRequest req;
    HttpServletResponse resp;
    HttpSession session;
    
    int gotStatus;
    ObjectMapper mapper;
    StringWriter sw;
    PrintWriter pw;

    @Before
    public void init() throws IOException {
        employeeServlet = Mockito.mock(EmployeeServlet.class, Mockito.CALLS_REAL_METHODS);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);

        mapper = new ObjectMapper();
        gotStatus = -1;
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        Mockito.when(resp.getWriter()).thenReturn(pw);
        Mockito.when(req.getSession(true)).thenReturn(session);

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
    public void testShouldSendError_whenNotLoggedIn() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_UNAUTHORIZED;

        Mockito.when(session.getAttribute("user")).thenReturn(null);

        employeeServlet.service(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are not authorized to access this endpoint.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testShouldSendError_whenUserNotEmployee() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_UNAUTHORIZED;

        Mockito.when(session.getAttribute("user")).thenReturn(null);

        employeeServlet.service(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are not authorized to access this endpoint.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }
}
