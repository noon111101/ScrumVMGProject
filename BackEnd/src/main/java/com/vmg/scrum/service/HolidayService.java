package com.vmg.scrum.service;

import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.payload.request.HolidayRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.payload.response.MessageResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface HolidayService {
    MessageResponse addHoliday(HolidayRequest holidayRequest) throws MessagingException, UnsupportedEncodingException;

    void updateHoliday(long id, HolidayRequest holidayRequest);
}
