package com.github.jaczerob.web.servlets.employee;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jaczerob.project1.web.servlets.employee.EmployeeServlet;
import com.github.jaczerob.utils.ServletTestHelper;

public class EmployeeServletTest extends ServletTestHelper {
    @Override
    protected void setServlet() {
        this.testServlet = Mockito.mock(EmployeeServlet.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testShouldSendError_whenNotLoggedIn() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_UNAUTHORIZED;

        Mockito.when(session.getAttribute("user")).thenReturn(null);
        this.testServlet.service(req, resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are not authorized to access this endpoint.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }

    @Test
    public void testShouldSendError_whenUserNotEmployee() throws ServletException, IOException {
        int wantStatus = HttpServletResponse.SC_UNAUTHORIZED;

        Mockito.when(session.getAttribute("user")).thenReturn(null);
        this.testServlet.service(req, resp);

        pw.flush();
        Map<String, String> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, String>>() {});
        Assert.assertEquals("You are not authorized to access this endpoint.", response.get("message"));
        Assert.assertEquals(wantStatus, this.gotStatus);
    }
}
