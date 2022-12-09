package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.payload.request.ManageRequests_Request;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.RequestRepository;import com.vmg.scrum.repository.ApproveSttRepository;
import com.vmg.scrum.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ApproveSttRepository approveSttRepository;

    @Override
    public Page<Request> ManageRequests(ManageRequests_Request manageRequests_request, Pageable pageable) {
        long userId = manageRequests_request.getUser_id();
        long departId = manageRequests_request.getDepart_id();
        long status = manageRequests_request.getStatus();
        String search = manageRequests_request.getSearch();
        Page<Request> pageRequests = null;

        if( departId!=0){
            if( status !=0 ){
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findByByDepartmentIdAndSearchAndStatus(departId, search, status,userId, pageable);
                }
                else{
                    pageRequests = requestRepository.findByByDepartmentIdAndStatus(departId, status, pageable);
                }
            }
            else{
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findByDepartmentIdAndSearch(departId, search, pageable);
                }
                else{
                    pageRequests = requestRepository.findByDepartmentId(departId, pageable);
                }
            }
        }
        else{
            if(status!=0 ){
//                Boolean available = Boolean.parseBoolean(status);
                if(search!=null && search!=""){
                    System.out.println("aaaa");
                    pageRequests = requestRepository.findBySearchAndStatus(search,status, pageable);
                }
                else{
                    pageRequests = requestRepository.findByStatus(status, pageable);
                }
            }
            else{
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findBySearch(search, pageable);
                }
                else{
                    pageRequests = requestRepository.findAll(pageable);
                }
            }

        }
        return pageRequests;
    }

    @Override
    public List<Request> MyRequests(Long id, Long status) {
        List<Request> requests = null;
        if(status!=null && status!=0){
            requests = requestRepository.findByRequestCreatorIdAndStatus(id, status);
        }
        else{
            requests = requestRepository.findByRequestCreatorId(id);
        }
        return requests;
    }

    @Override
    public MessageResponse changeApproveStatus(long id, long status) {
        Request request = requestRepository.findByRequestId(id);
        ApproveStatus approveStatus = approveSttRepository.findById(status);
        request.setApproveStatus(approveStatus);
        requestRepository.save(request);
        if(request.getApproveStatus().getId()==1){
            return  new MessageResponse("Đã hoàn tác");
        }
        if(request.getApproveStatus().getId()==2){
            return  new MessageResponse("Đã chấp thuận");
        }
        if(request.getApproveStatus().getId()==3){
            return  new MessageResponse("Đã từ chối");
        }
        if(request.getApproveStatus().getId()==4){
            return  new MessageResponse("Đã quá hạn");
        }
        if(request.getApproveStatus().getId()==5){
            return  new MessageResponse("Đã hủy");
        }
        if(request.getApproveStatus().getId()==6){
            return  new MessageResponse("Đã hoàn thành");
        }

        return null;
    }
}
