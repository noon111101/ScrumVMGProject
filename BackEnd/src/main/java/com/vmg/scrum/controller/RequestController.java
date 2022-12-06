package com.vmg.scrum.controller;

import com.vmg.scrum.model.excel.LogDetail;
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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("")
    public ResponseEntity<Page<Request>> getRequests(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "30") int size,
                                                     @ModelAttribute ManageRequests_Request manageRequests_request) throws ParseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> pageRequests = requestService.ManageRequests(manageRequests_request, pageable);
        return new ResponseEntity<>(pageRequests, HttpStatus.OK);
    }

//    @GetMapping("")
//    public ResponseEntity<Page<Request>> getRequests(@RequestParam(name = "page", defaultValue = "0") int page,
//                                                     @RequestParam(name = "size", defaultValue = "30") int size,
//                                                     @RequestParam(name = "depart_id", defaultValue = "0") Long departId,
//                                                     @RequestParam(name = "search", required = false) String search,
//                                                     @RequestParam(name = "status", defaultValue = "0") Long status) throws ParseException {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Request> pageRequests = null;
//
//        if(departId!=null && departId!=0){
//            if(status!=null && status !=0 ){
//                if(search!=null && search!=""){
//                    pageRequests = requestRepository.findByByDepartmentIdAndSearchAndStatus(departId, search, status,pageable);
//                }
//                else{
//                    pageRequests = requestRepository.findByByDepartmentIdAndStatus(departId, status, pageable);
//                }
//            }
//            else{
//                if(search!=null && search!=""){
//                    pageRequests = requestRepository.findByDepartmentIdAndSearch(departId, search, pageable);
//                }
//                else{
//                    pageRequests = requestRepository.findByDepartmentId(departId, pageable);
//                }
//            }
//        }
//        else{
//            if(status!=null ){
////                Boolean available = Boolean.parseBoolean(status);
//                if(search!=null && search!=""){
//                    System.out.println("aaaa");
//                    pageRequests = requestRepository.findBySearchAndStatus(search,status, pageable);
//                }
//                else{
//                    pageRequests = requestRepository.findByStatus(status, pageable);
//                }
//            }
//            else{
//                if(search!=null && search!=""){
//                    pageRequests = requestRepository.findBySearch(search, pageable);
//                }
//                else{
//                    pageRequests = requestRepository.findAll(pageable);
//                }
//            }
//
//        }
//        return new ResponseEntity<>(pageRequests, HttpStatus.OK);
//    }
}
