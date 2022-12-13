package com.vmg.scrum.service;


import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.payload.request.HolidayRequest;
import com.vmg.scrum.payload.request.ManageHoliday_Request;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface HolidayService {
    MessageResponse addHoliday(HolidayRequest holidayRequest) throws MessagingException, UnsupportedEncodingException;

    void updateHoliday(long id, HolidayRequest holidayRequest);

    MessageResponse deleteHoliday(Long id);

    Page<Holiday> pageHolidays(String search, Pageable pageable);


    Page<Holiday> manageHolidays(ManageHoliday_Request manageHoliday_request, Pageable pageable);
}
