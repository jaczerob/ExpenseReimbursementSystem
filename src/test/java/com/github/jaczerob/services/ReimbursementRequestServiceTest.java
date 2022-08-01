package com.github.jaczerob.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ResolvedReimbursementRequest;
import com.github.jaczerob.project1.models.users.Manager;
import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.services.ReimbursementRequestService;

public class ReimbursementRequestServiceTest {
    ReimbursementRequestRepository reimbursementRequestRepository;
    ReimbursementRequestService reimbursementRequestService;

    @Before
    public void init() {
        reimbursementRequestRepository = Mockito.mock(ReimbursementRequestRepository.class);
        reimbursementRequestService = new ReimbursementRequestService(reimbursementRequestRepository);
    }

    @Test
    public void testGetReimbursementRequestSuccess() {
        ReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        Mockito.when(this.reimbursementRequestRepository.get(1)).thenReturn(Optional.of(request));
        Assert.assertEquals(request, this.reimbursementRequestService.getReimbursementRequest(1).get());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetReimbursementRequestFail_whenIDLessThan0() {
        this.reimbursementRequestService.getReimbursementRequest(-1);
    }

    @Test
    public void testResolveReimbursementRequestSuccess() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        Mockito.when(this.reimbursementRequestRepository.get(request.getID())).thenReturn(Optional.of(request));

        this.reimbursementRequestService.resolveReimbursementRequest(request, true, Mockito.mock(Manager.class));
        Assert.assertEquals(request, this.reimbursementRequestService.getReimbursementRequest(request.getID()).get());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveReimbursementRequestFail_whenIDLessThanZero() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(-1, 1, 1f, "money");
        this.reimbursementRequestService.resolveReimbursementRequest(request, true, Mockito.mock(Manager.class));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveReimbursementRequestFail_whenAmountLessThan1() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(1, 1, 0f, "money");
        this.reimbursementRequestService.resolveReimbursementRequest(request, true, Mockito.mock(Manager.class));
    }

    @Test
    public void testAddReimbursementRequestSuccess() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        Mockito.when(this.reimbursementRequestRepository.get(request.getID())).thenReturn(Optional.of(request));
        
        this.reimbursementRequestService.addReimbursementRequest(request);
        Assert.assertEquals(request, this.reimbursementRequestService.getReimbursementRequest(request.getID()).get());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddReimbursementRequestFail_whenIDLessThanZero() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(-1, 1, 1f, "money");
        this.reimbursementRequestService.addReimbursementRequest(request);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddReimbursementRequestFail_whenAmountLessThan1() throws RecordNotExistsException, IllegalArgumentException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(1, 1, 0f, "money");
        this.reimbursementRequestService.addReimbursementRequest(request);
    }

    @Test
    public void testGetAllPendingReimbursementsSuccess() {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<ReimbursementRequest>() {
            {
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(PendingReimbursementRequest.class));
            }
        };

        Mockito.when(this.reimbursementRequestRepository.getAllFromStatus(true)).thenReturn(reimbursementRequests);
        Assert.assertEquals(reimbursementRequests, this.reimbursementRequestService.getAllPendingReimbursements());
    }

    @Test
    public void testGetAllResolvedReimbursementsSuccess() {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<ReimbursementRequest>() {
            {
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
            }
        };

        Mockito.when(this.reimbursementRequestRepository.getAllFromStatus(false)).thenReturn(reimbursementRequests);
        Assert.assertEquals(reimbursementRequests, this.reimbursementRequestService.getAllResolvedReimbursements());
    }

    @Test
    public void testGetAllFromEmployeeSuccess() {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<ReimbursementRequest>() {
            {
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(PendingReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
                add(Mockito.mock(ResolvedReimbursementRequest.class));
            }
        };

        Mockito.when(this.reimbursementRequestRepository.getAllFromEmployee(1)).thenReturn(reimbursementRequests);
        Assert.assertEquals(reimbursementRequests, this.reimbursementRequestService.getAllEmployeeReimbursements(1));
    }
}
