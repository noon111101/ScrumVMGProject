package com.vmg.scrum.controller;

import com.vmg.scrum.model.request.CategoryReason;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.ManageRequests_Request;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.*;
import com.vmg.scrum.service.OfferRequestService;
import com.vmg.scrum.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    OfferRequestRepository offerRepository;


    @Autowired
    private CategoryReasonRepository categoryReasonRepository;
    private final OfferRequestService offerService;

    public RequestController(OfferRequestService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("")
    public ResponseEntity<?> addRequest(@Valid @ModelAttribute OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(offerService.addRequest(offerRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<Request>> getRequests(@ModelAttribute ManageRequests_Request manageRequests_request) throws ParseException {
        List<Request> pageRequests = requestService.ManageRequests(manageRequests_request);
        return new ResponseEntity<>(pageRequests, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable("id") long id) throws ParseException {
        return new ResponseEntity<>(requestRepository.findByRequestId(id), HttpStatus.OK);
    }

    @GetMapping("/creator")
    public ResponseEntity<List<Request>> getRequestByCreatorId(@RequestParam(name = "id") long id,
                                                               @RequestParam(name = "statusId", defaultValue = "0")long statusId) throws ParseException {
        return new ResponseEntity<>(requestService.MyRequests(id,statusId), HttpStatus.OK);
    }

    @GetMapping("/categoryreason/{id}")
    public ResponseEntity<List<CategoryReason>> getCategoryReason(@PathVariable("id") long id) throws ParseException {
        return new ResponseEntity<>(categoryReasonRepository.getCategoryReasonByCatergoryRequest_Id(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<MessageResponse> changeStatus(@RequestParam(name = "requestId")long requestId,
                                                        @RequestParam(name = "newStatusId")long newStatusId,
                                                        @RequestParam(name = "oldStatusId")long oldStatusId) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(requestService.changeApproveStatus(requestId,newStatusId,oldStatusId));
    }
}
