package com.github.jaczerob.web.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.context.ContextListener;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ContextListenerTest {
    @Mock(answer=Answers.CALLS_REAL_METHODS) private DataSource dataSource;
    @Mock private ServletContext context;
    @Mock private ServletContextEvent event;
    @Mock private Connection conn;

    private ServletContextListener listener;
    private HashMap<String, Object> attributes;

    @Before
    public void init() {
        listener = new ContextListener();
        attributes = new HashMap<String, Object>();
    }

    @Test
    public void testContextInitializedSuccess() throws SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(conn);

        Mockito.when(event.getServletContext()).thenReturn(context);
        Mockito.when(context.getRealPath("/WEB-INF/classes/mail.properties")).thenReturn("src/test/resources/mail.properties");
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String attributeName = args[0].toString();
                UserService userService = (UserService) args[1];
                attributes.put(attributeName, userService);
                return null;
            }
        }).when(context).setAttribute(Mockito.matches("userService"), Mockito.isA(UserService.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String attributeName = args[0].toString();
                ReimbursementRequestService reimbursementRequestService = (ReimbursementRequestService) args[1];
                attributes.put(attributeName, reimbursementRequestService);
                return null;
            }
        }).when(context).setAttribute(Mockito.matches("reimbursementRequestService"), Mockito.isA(ReimbursementRequestService.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String attributeName = args[0].toString();
                MailService mailService = (MailService) args[1];
                attributes.put(attributeName, mailService);
                return null;
            }
        }).when(context).setAttribute(Mockito.matches("mailService"), Mockito.isA(MailService.class));

        listener.contextInitialized(event);
        Assert.assertNotNull(attributes.get("userService"));
        Assert.assertNotNull(attributes.get("reimbursementRequestService"));
        Assert.assertNotNull(attributes.get("mailService"));
    }
}
