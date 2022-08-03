package com.github.jaczerob.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.servlets.Servlet;

@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class ServletTestHelper {
    protected Servlet testServlet;

    @Mock protected ServletContext context;
    @Mock protected HttpServletRequest req;
    @Mock protected HttpServletResponse resp;
    @Mock protected HttpSession session;

    @Mock protected ReimbursementRequestService reimbursementRequestService;
    @Mock protected UserService userService;
    @Mock protected MailService mailService;

    protected ObjectMapper objectMapper;
    protected int gotStatus;
    protected StringWriter sw;
    protected PrintWriter pw;

    abstract protected void setServlet();

    @Before
    public void baseInit() throws IOException, ServletException {
        this.setServlet();

        this.objectMapper = new ObjectMapper();
        this.gotStatus = -1;
        this.sw = new StringWriter();
        this.pw = new PrintWriter(sw);

        Mockito.doReturn(this.context).when(this.testServlet).getServletContext();
        Mockito.when(this.context.getAttribute("reimbursementRequestService")).thenReturn(this.reimbursementRequestService);
        Mockito.when(this.context.getAttribute("userService")).thenReturn(this.userService);
        Mockito.when(this.context.getAttribute("mailService")).thenReturn(this.mailService);
        Mockito.when(this.context.getAttribute("objectMapper")).thenReturn(new ObjectMapper());
        Mockito.when(this.resp.getWriter()).thenReturn(pw);
        Mockito.when(this.req.getSession(Mockito.anyBoolean())).thenReturn(this.session);
        Mockito.doAnswer(new Answer<Void>() {
            @Override public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer responseStatus = (Integer) args[0];
                gotStatus = responseStatus;
                return null;
            }
        }).when(this.resp).setStatus(Mockito.anyInt());

        this.testServlet.init();
    }
}
