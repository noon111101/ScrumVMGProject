package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.ManageRequests_Request;
import com.vmg.scrum.repository.RequestRepository;
import com.vmg.scrum.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public Page<Request> ManageRequests(ManageRequests_Request manageRequests_request, Pageable pageable) {
        long departId = manageRequests_request.getDepart_id();
        long status = manageRequests_request.getStatus();
        String search = manageRequests_request.getSearch();
        Page<Request> pageRequests = null;

        if( departId!=0){
            if( status !=0 ){
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findByByDepartmentIdAndSearchAndStatus(departId, search, status,pageable);
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
}
