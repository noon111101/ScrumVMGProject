package com.vmg.scrum.service;

import com.vmg.scrum.payload.response.MessageResponse;

public interface HolidayService {

    MessageResponse deleteHoliday(Long id);
}
