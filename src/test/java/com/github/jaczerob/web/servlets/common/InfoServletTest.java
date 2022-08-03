package com.github.jaczerob.web.servlets.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.web.servlets.employee.InfoServlet;
import com.github.jaczerob.utils.ServletTestHelper;

public class InfoServletTest extends ServletTestHelper {
    @Override
    protected void setServlet() {
        this.testServlet = Mockito.mock(InfoServlet.class, Mockito.CALLS_REAL_METHODS);
    }

    @Before
    public void init() {
        Mockito.when(this.reimbursementRequestService.getAllResolvedEmployeeReimbursements(Mockito.anyInt())).thenReturn(Arrays.asList(new ReimbursementRequest[]{}));
        Mockito.when(this.reimbursementRequestService.getAllPendingEmployeeReimbursements(Mockito.anyInt())).thenReturn(Arrays.asList(new ReimbursementRequest[]{}));
    }

    @Test
    public void testInfoSuccess() throws ServletException, IOException {
        User user = new Employee(1, "email", "username", "password");
        Mockito.when(session.getAttribute("user")).thenReturn(user);
        
        int wantStatus = HttpServletResponse.SC_OK;

        this.testServlet.doGet(req, resp);

        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals("Here is your user information!", response.get("message"));
        Assert.assertEquals(user.getEmail(), response.get("email"));
        Assert.assertEquals(user.getUsername(), response.get("username"));
        Assert.assertEquals(user.getID(), response.get("id"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }
}
