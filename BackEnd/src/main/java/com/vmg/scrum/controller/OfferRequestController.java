package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.repository.OfferRequestRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.OfferRequestService;
import com.vmg.scrum.service.UserService;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/request")
public class OfferRequestController {

    @Autowired
    OfferRequestRepository offerRepository;

    @Autowired
    private OfferRequestService offerRequestService;

    @Autowired
    private UserRepository userRepository;
    private final OfferRequestService offerService;

    public OfferRequestController(OfferRequestService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("")
    public ResponseEntity<?> addRequest(@Valid @ModelAttribute OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(offerService.addRequest(offerRequest));
    }

}
