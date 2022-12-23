package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.Holiday;
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
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
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
    private final HolidayRepository holidayRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    MailService mailService;


    @Override
    public MessageResponse addRequest(OfferRequest offerRequest) throws MessagingException, UnsupportedEncodingException {
        if (offerRequest.getTimeEnd() != null && offerRequest.getTimeStart() != null && offerRequest.getDateForget() == null) {
            if (offerRequest.getDateFrom().isAfter(offerRequest.getDateTo())) {
                throw new RuntimeException("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
            }

            if (offerRequest.getDateFrom().equals(offerRequest.getDateTo())) {
                if (!offerRequest.getTimeEnd().isAfter(offerRequest.getTimeStart())) {
                    throw new RuntimeException("Trong một ngày giờ bắt đầu phải lớn giờ kết thúc");
                }
            }

            List<Holiday> holidays = holidayRepository.findAll();
            for (Holiday h : holidays) {
                if ((!offerRequest.getDateFrom().isBefore(h.getDateFrom()) && !offerRequest.getDateFrom().isAfter(h.getDateTo()) && !offerRequest.getDateTo().isBefore(h.getDateTo()))
                        || (!offerRequest.getDateFrom().isAfter(h.getDateFrom()) && !offerRequest.getDateTo().isBefore(h.getDateFrom()) && !offerRequest.getDateTo().isAfter(h.getDateTo()))
                        || (offerRequest.getDateFrom().isBefore(h.getDateFrom()) && offerRequest.getDateTo().isAfter(h.getDateTo()))) {
                    throw new RuntimeException("Thời gian nghỉ không được trùng ngày nghỉ lễ (" + h.getHolidayName() + ")");
                }
            }
        }


        User creator = userRepository.findByUserName(offerRequest.getCreator());
        String department = userRepository.findDepartmentByUserID(offerRequest.getCreator());
        LocalDate dateFrom = offerRequest.getDateFrom();
        LocalDate dateTo = offerRequest.getDateTo();
        LocalTime timeStart = offerRequest.getTimeStart();
        LocalTime timeEnd = offerRequest.getTimeEnd();
        LocalDate dateForget = offerRequest.getDateForget();
//        Request totalsday =  requestRepository.findByRequestId(offerRequest.getId());

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
            System.out.println(request.getTotalDays() + "," + Long.valueOf(request.getDateFrom().getYear()) + "*" + Long.valueOf(request.getDateFrom().getDayOfMonth()));
            Furlough furlough = furloughRepository.findByYearAndUserIdAndMonthInYear(Long.valueOf(request.getDateFrom().getYear()), request.getCreator().getId(), Long.valueOf(request.getDateFrom().getMonthValue()));
            if (furlough.getAvailableUsedTillMonth() >= request.getTotalDays() && furlough != null) {
                System.out.println("OK");
            } else {
                throw new RuntimeException("Không đủ ngày phép để tạo yêu cầu nghỉ phép");
            }
        }

        request.setApprovers(approves);
        request.setFollowers(followers);
        offerRepository.save(request);
        if(offerRequest.getCategoryReason() == 6) {
            mailService.sendEmailFollowersForget(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, dateForget);
            mailService.sendEmailApproversForget(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, dateForget);
        } else if (offerRequest.getCategoryReason() == 4 || offerRequest.getCategoryReason() == 5) {
            mailService.sendEmailFollowersTCS(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, dateFrom, dateTo);
            mailService.sendEmailApproversTCS(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, dateFrom, dateTo);
    }else {
            mailService.sendEmailFollowers(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, request, timeStart, dateFrom, timeEnd, dateTo);
            mailService.sendEmailApprovers(offerRequest.getFollowers(), offerRequest.getTitle(), department, creator, request, timeStart, dateFrom, timeEnd, dateTo);
        }

        return new MessageResponse("Tạo request thành công!");


    }
}
