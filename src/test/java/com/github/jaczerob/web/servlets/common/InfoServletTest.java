package com.github.jaczerob.web.servlets.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

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
import com.github.jaczerob.project1.web.servlets.common.InfoServlet;

public class InfoServletTest {
    InfoServlet infoServlet;
    ServletConfig config;
    ServletContext context;
    HttpServletRequest req;
    HttpServletResponse resp;
    HttpSession session;

    int gotStatus;
    StringWriter sw;
    PrintWriter pw;
    ObjectMapper mapper;

    @Before
    public void init() throws IOException, ServletException {
        infoServlet = Mockito.spy(new InfoServlet());
        config = Mockito.mock(ServletConfig.class);
        context = Mockito.mock(ServletContext.class);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        
        gotStatus = -1;
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        mapper = new ObjectMapper();

        Mockito.doReturn(config).when(infoServlet).getServletConfig();
        Mockito.doReturn(context).when(infoServlet).getServletContext();
        Mockito.when(context.getAttribute("objectMapper")).thenReturn(mapper);
        Mockito.when(resp.getWriter()).thenReturn(pw);
        Mockito.when(req.getSession(true)).thenReturn(session);
        infoServlet.init();

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
    public void testInfoSuccess() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        int wantStatus = HttpServletResponse.SC_OK;

        Mockito.when(session.getAttribute("user")).thenReturn(user);

        infoServlet.doGet(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("Here is your user information!", response.get("message"));
        Assert.assertEquals(user.getEmail(), response.get("email"));
        Assert.assertEquals(user.getUsername(), response.get("username"));
        Assert.assertEquals(String.valueOf(user.getID()), response.get("id"));
        Assert.assertEquals(wantStatus, gotStatus);
    }

    @Test
    public void testInfoFail_whenNotLoggedIn() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_UNAUTHORIZED;

        Mockito.when(session.getAttribute("user")).thenReturn(null);

        infoServlet.doGet(req, resp);

        pw.flush();
        Map<String, String> response = mapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are not authorized to access this endpoint.", response.get("message"));
        Assert.assertEquals(wantStatus, gotStatus);
    }
}
