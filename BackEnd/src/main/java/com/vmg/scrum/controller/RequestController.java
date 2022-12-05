package com.vmg.scrum.controller;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("")
    public ResponseEntity<Page<Request>> getRequests(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "30") int size,
                                                     @RequestParam(name = "id", defaultValue = "0") Long departid,
                                                     @RequestParam(name = "search", required = false) String search,
                                                     @RequestParam(name = "status", required = false) Long status) throws ParseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> pageRequests = null;

        if(departid!=null && departid!=0){
            if(status!=null && status !=0 ){
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findByByDepartmentIdAndSearchAndStatus(departid, search, status,pageable);
                }
                else{
                    pageRequests = requestRepository.findByByDepartmentIdAndStatus(departid, status, pageable);
                }
            }
            else{
                if(search!=null && search!=""){
                    pageRequests = requestRepository.findByDepartmentIdAndSearch(departid, search, pageable);
                }
                else{
                    pageRequests = requestRepository.findByDepartmentId(departid, pageable);
                }
            }
        }
        else{
            if(status!=null ){
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
        return new ResponseEntity<>(pageRequests, HttpStatus.OK);
    }
}
