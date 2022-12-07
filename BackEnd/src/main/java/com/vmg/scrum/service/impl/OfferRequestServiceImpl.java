package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.model.request.CatergoryRequest;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.ApproveSttRepository;
import com.vmg.scrum.repository.CategoryRequestRepository;
import com.vmg.scrum.repository.OfferRequestRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.OfferRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OfferRequestServiceImpl implements OfferRequestService {

    @Autowired
    private final OfferRequestRepository offerRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ApproveSttRepository approveRepository;

    @Autowired
    private final CategoryRequestRepository categoryRepository;


    public OfferRequestServiceImpl(OfferRequestRepository offerRepository, UserRepository userRepository, ApproveSttRepository approveRepository, CategoryRequestRepository categoryRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.approveRepository = approveRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public MessageResponse addRequest(OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
        User creator = userRepository.findByUserName(offerRequest.getCreator());
        ApproveStatus approveStatus = approveRepository.findById(offerRequest.getApproveStatus());
        CatergoryRequest catergoryRequest = categoryRepository.findById(offerRequest.getCatergoryRequest());

        Request request = new Request(creator, offerRequest.getTitle(), offerRequest.getContent(), approveStatus, catergoryRequest, offerRequest.getDateFrom(), offerRequest.getDateTo(), offerRequest.getTimeStart(), offerRequest.getTimeEnd(), offerRequest.getLastSign());
        Set<User> approves = new HashSet<>();
        Set<User> followers = new HashSet<>();

        for (String s : offerRequest.getApprovers()) {
           User userApprove = userRepository.getByUsername(s);
            approves.add(userApprove);
        }

        for (String s : offerRequest.getFollowers()) {
            User userFolower = userRepository.getByUsername(s);
            followers.add(userFolower);
        }
        request.setApprovers(approves);
        request.setFollowers(followers);
        offerRepository.save(request);
        return new MessageResponse("Tạo request thành công!");
    }
}
