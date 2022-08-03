package com.github.jaczerob.web.servlets.manager;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ResolvedReimbursementRequest;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.Manager;
import com.github.jaczerob.project1.web.servlets.manager.RequestDenyServlet;
import com.github.jaczerob.utils.ServletTestHelper;

public class RequestDenyServletTest extends ServletTestHelper {
    @Mock protected Manager manager;

    @Override
    protected void setServlet() {
        this.testServlet = Mockito.mock(RequestDenyServlet.class, Mockito.CALLS_REAL_METHODS);
    }

    @Before
    public void init() {
        Mockito.when(this.req.getRequestURI()).thenReturn("/request/deny/1");
        Mockito.when(this.session.getAttribute("user")).thenReturn(manager);
    }

    @Test
    public void testRequestDenyFail_whenNoIDSupplied() throws ServletException, IOException {
        Mockito.when(this.req.getRequestURI()).thenReturn("/request/deny");
        this.testServlet.doPost(this.req, this.resp);

        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "Please set an ID of a pending reimbursement request. These can be retrieved from /request/view/pending. To use, do /request/accept/{id} or /request/deny/{id}.", 
            response.get("message")
        );
    }

    @Test
    public void testRequestDenyFail_whenIDTypeError() throws ServletException, IOException {
        Mockito.when(this.req.getRequestURI()).thenReturn("/request/deny/a");
        this.testServlet.doPost(this.req, this.resp);

        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "Error: ID must be a integer.",
            response.get("message")
        );

        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, this.gotStatus);
    }

    @Test
    public void testRequestDenyFail_whenRequestResolved() throws ServletException, IOException {
        Mockito.when(this.reimbursementRequestService.getReimbursementRequest(1)).thenReturn(Optional.of(Mockito.mock(ResolvedReimbursementRequest.class)));
        this.testServlet.doPost(this.req, this.resp);
        
        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "This request was already resolved.",
            response.get("message")
        );

        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, this.gotStatus);
    }

    @Test
    public void testRequestDenyFail_whenRequestNotFound() throws ServletException, IOException, RecordNotExistsException, IllegalArgumentException {
        Mockito.when(this.reimbursementRequestService.getReimbursementRequest(1)).thenReturn(Optional.of(Mockito.mock(PendingReimbursementRequest.class)));
        Mockito.doThrow(RecordNotExistsException.class).when(this.reimbursementRequestService).resolveReimbursementRequest(Mockito.any(), Mockito.anyBoolean(), Mockito.any());
        this.testServlet.doPost(this.req, this.resp);
        
        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "This request does not exist.",
            response.get("message")
        );

        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, this.gotStatus);
    }

    @Test
    public void testRequestDenyFail_whenUserNotFound() throws ServletException, IOException {
        Mockito.when(this.reimbursementRequestService.getReimbursementRequest(1)).thenReturn(Optional.of(Mockito.mock(PendingReimbursementRequest.class)));
        Mockito.when(this.userService.getUser(Mockito.anyInt())).thenReturn(Optional.empty());
        this.testServlet.doPost(this.req, this.resp);
        
        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "Sorry, there was an internal server error. Please try again later.",
            response.get("message")
        );
        
        Assert.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, this.gotStatus);
    }

    @Test
    public void testRequestDenySuccess() throws ServletException, IOException {
        Mockito.when(this.reimbursementRequestService.getReimbursementRequest(1)).thenReturn(Optional.of(Mockito.mock(PendingReimbursementRequest.class)));
        Mockito.when(this.userService.getUser(Mockito.anyInt())).thenReturn(Optional.of(Mockito.mock(Employee.class)));
        this.testServlet.doPost(this.req, this.resp);
        
        this.pw.flush();
        Map<String, Object> response = this.objectMapper.readValue(sw.toString(), new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals(
            "This request was successfully resolved.",
            response.get("message")
        );
        
        Assert.assertEquals(HttpServletResponse.SC_OK, this.gotStatus);
    }
}