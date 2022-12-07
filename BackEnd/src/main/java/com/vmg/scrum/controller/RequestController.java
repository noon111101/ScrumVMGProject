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

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable("id") long id) throws ParseException {
        return new ResponseEntity<>(requestRepository.findByRequestId(id), HttpStatus.OK);
    }

//
}
