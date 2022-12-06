package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.OfferRequestRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.OfferRequestService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class OfferRequestServiceImpl implements OfferRequestService {
    private final OfferRequestRepository offerRepository;

    private final UserRepository userRepository;


    public OfferRequestServiceImpl(OfferRequestRepository offerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

//    @Override
//    public MessageResponse addRequest(OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
//        Optional<User> creator = userRepository.findById(offerRequest.getCreator().getId());
//        Request request = new Request(offerRequest.getTitle(), offerRequest.getContent(), offerRequest.getCatergoryRequest(), offerRequest.getDateFrom(), offerRequest.getDateTo(), offerRequest.getTimeStart(),offerRequest.getTimeEnd(), offerRequest.getLastSign());
//        Set<User> approves = new HashSet<>();
//        Set<User> followers = new HashSet<>();
//
////        User userApprove = userRepository.findById(offerRequest.getApprovers());
//
//    }
}
