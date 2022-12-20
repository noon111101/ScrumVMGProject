package com.vmg.scrum.service;

import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.ManageRequests_Request;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {

    List<Request> ManageRequests(ManageRequests_Request manageRequests_request);

    List<Request> MyRequests(Long id, Long status);
    MessageResponse changeApproveStatus(long id, long newStatus, long oldStatus, long approvedId);
}
