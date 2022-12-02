package com.vmg.scrum.controller;

import com.vmg.scrum.repository.OfferRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request")
public class OfferRequestController {

    @Autowired
    OfferRequestRepository offerRepository;


}
