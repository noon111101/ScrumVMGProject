package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.OfferRequestRepository;
import com.vmg.scrum.service.OfferRequestService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public class OfferRequestServiceImpl implements OfferRequestService {
    private final OfferRequestRepository offerRepository;


    public OfferRequestServiceImpl(OfferRequestRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

//    @Override
//    public MessageResponse addRequest(OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
//        Request request = new Request(offerRequest.getTitle(), offerRequest.getCreator(), offerRequest.getContent(), offerRequest.getCatergoryRequest(), offerRequest.getDateFrom(), offerRequest.getDateTo(), offerRequest.getSession(), offerRequest.getLastSign());
//        Set<String> strApproves = offerRequest.getApprovers();
//        Set<User> approves = new HashSet<>();
//        Set<String> strFollowers = offerRequest.getFollowers();
//        Set<User> followers = new HashSet<>();
//
//    }


}
