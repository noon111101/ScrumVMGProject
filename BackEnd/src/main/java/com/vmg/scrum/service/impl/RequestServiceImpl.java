package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.payload.request.ManageRequests_Request;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.FurloughRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.RequestRepository;
import com.vmg.scrum.repository.ApproveSttRepository;
import com.vmg.scrum.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ApproveSttRepository approveSttRepository;

    @Autowired
    private final FurloughRepository furloughRepository;

    @Autowired
    private final LogDetailRepository logDetailRepository;

    @Override
    public List<Request> ManageRequests(ManageRequests_Request manageRequests_request) {
        long userId = manageRequests_request.getUser_id();
        long departId = manageRequests_request.getDepart_id();
        long status = manageRequests_request.getStatus();
        String search = manageRequests_request.getSearch();
        List<Request> requests = null;

        if (departId != 0) {
            if (status != 0) {
                if (search != null && search != "") {
                    requests = requestRepository.findByByDepartmentIdAndSearchAndStatus(userId, departId, search, status);
                } else {
                    requests = requestRepository.findByByDepartmentIdAndStatus(userId, departId, status);
                }
            } else {
                if (search != null && search != "") {
                    requests = requestRepository.findByDepartmentIdAndSearch(userId, departId, search);
                } else {
                    requests = requestRepository.findByDepartmentId(userId, departId);
                }
            }
        } else {
            if (status != 0) {
//                Boolean available = Boolean.parseBoolean(status);
                if (search != null && search != "") {
                    requests = requestRepository.findBySearchAndStatus(userId, search, status);
                } else {
                    requests = requestRepository.findByStatus(userId, status);
                }
            } else {
                if (search != null && search != "") {
                    requests = requestRepository.findBySearch(userId, search);
                } else {
                    requests = requestRepository.findAll(userId);
                }
            }

        }
        return requests;
    }

    @Override
    public List<Request> MyRequests(Long id, Long status) {
        List<Request> requests = null;
        if (status != null && status != 0) {
            requests = requestRepository.findByRequestCreatorIdAndStatus(id, status);
        } else {
            requests = requestRepository.findByRequestCreatorId(id);
        }
        return requests;
    }

    @Override
    public MessageResponse changeApproveStatus(long id, long newStatus, long oldStatus) {
        Request request = requestRepository.findByRequestId(id);
        ApproveStatus approveStatus = approveSttRepository.findById(newStatus);
        request.setApproveStatus(approveStatus);
        requestRepository.save(request);
        if (request.getApproveStatus().getId() == 1) {
//            LogDetail logDetail = logDetailRepository.findByDateAndUser(request.getCreator().getId(), request.getDateFrom());
//            if(logDetail.isRequestActive()){
//                throw new RuntimeException("Không thể hoàn tác");
//            }
            if (request.getCategoryReason().getId() == 1 && oldStatus == 2) {
                Furlough furlough = (Furlough) furloughRepository.findByYearAndUserIdAndMonthInYear((long) request.getDateFrom().getYear(), request.getCreator().getId(), (long) request.getDateFrom().getMonthValue());
                furlough.setAvailableUsedTillMonth((float) (furlough.getAvailableUsedTillMonth() + request.getTotalDays()));
                furlough.setUsedInMonth((float) (furlough.getUsedInMonth() - request.getTotalDays()));
                furloughRepository.save(furlough);
            }


            return new MessageResponse("Đã hoàn tác");
        }
        if (request.getApproveStatus().getId() == 2) {
            if (request.getCategoryReason().getId() == 1) {
                Furlough furlough = (Furlough) furloughRepository.findByYearAndUserIdAndMonthInYear((long) request.getDateFrom().getYear(), request.getCreator().getId(), (long) request.getDateFrom().getMonthValue());
                if (furlough.getAvailableUsedTillMonth() >= request.getTotalDays()) {
                    furlough.setAvailableUsedTillMonth((float) (furlough.getAvailableUsedTillMonth() - request.getTotalDays()));
                    furlough.setUsedInMonth((float) (furlough.getUsedInMonth() + request.getTotalDays()));
                    furloughRepository.save(furlough);
                } else {
                    throw new RuntimeException("Nhân viên không đủ ngày phép!");
                }
            }
            return new MessageResponse("Đã chấp thuận");
        }
        if (request.getApproveStatus().getId() == 3) {
            return new MessageResponse("Đã từ chối");
        }
        if (request.getApproveStatus().getId() == 4) {
            return new MessageResponse("Đã hủy");
        }
        return null;
    }
}
