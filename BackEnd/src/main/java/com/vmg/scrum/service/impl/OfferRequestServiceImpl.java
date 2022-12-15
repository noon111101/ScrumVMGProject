package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.model.request.CategoryReason;
import com.vmg.scrum.model.request.CatergoryRequest;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.payload.request.OfferRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.*;
import com.vmg.scrum.service.MailService;
import com.vmg.scrum.service.OfferRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OfferRequestServiceImpl implements OfferRequestService {

    @Autowired
    private final OfferRequestRepository offerRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ApproveSttRepository approveRepository;

    @Autowired
    private final CategoryRequestRepository categoryRequestRepository;

    @Autowired
    private final CategoryReasonRepository categoryReasonRepository;

    @Autowired
    private final FurloughRepository furloughRepository;
    @Autowired
    MailService mailService;


    @Override
    public MessageResponse addRequest(OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
        if (offerRequest.getDateFrom().isAfter(offerRequest.getDateTo())) {
            throw new RuntimeException("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
        }
        User creator = userRepository.findByUserName(offerRequest.getCreator());
        ApproveStatus approveStatus = approveRepository.findById(offerRequest.getApproveStatus());
        CatergoryRequest catergoryRequest = categoryRequestRepository.findById(offerRequest.getCatergoryRequest());
        CategoryReason categoryReason = categoryReasonRepository.findById(offerRequest.getCategoryReason());
        Request request = new Request(creator, offerRequest.getTitle(), offerRequest.getContent(), approveStatus, categoryReason, catergoryRequest, offerRequest.getDateFrom(), offerRequest.getDateTo(), offerRequest.getDateForget(), offerRequest.getTimeStart(), offerRequest.getTimeEnd(), offerRequest.getLastSign());
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

        if (request.getCategoryReason().getId() == 1) {
            if (request.getTotalDays() == 0) {
                throw new RuntimeException("Thông tin ngày giờ nghỉ không hợp lệ");
            }
            System.out.println(request.getTotalDays()+","+Long.valueOf(request.getDateFrom().getYear())+"*"+Long.valueOf(request.getDateFrom().getDayOfMonth()));
            Furlough furlough =  furloughRepository.findByYearAndUserIdAndMonthInYear(Long.valueOf(request.getDateFrom().getYear()), request.getCreator().getId(), Long.valueOf(request.getDateFrom().getMonthValue()));
            if (furlough.getAvailableUsedTillMonth() >= request.getTotalDays() && furlough!= null) {
                System.out.println("OK");
            }
            else {
                throw new RuntimeException("Không đủ ngày phép để tạo yêu cầu nghỉ phép");
            }
        }

        request.setApprovers(approves);
        request.setFollowers(followers);
        offerRepository.save(request);
//        mailService.sendEmailFollowers(offerRequest.getFollowers(), offerRequest.getTitle(), fullName);
//        mailService.sendEmailApprovers(offerRequest.getApprovers(), offerRequest.getTitle(), fullName);
        return new MessageResponse("Tạo request thành công!");


    }
}
