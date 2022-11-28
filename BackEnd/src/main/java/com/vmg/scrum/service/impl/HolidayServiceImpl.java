package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    HolidayRepository holidayRepository;
    @Override
    public MessageResponse deleteHoliday(Long id) {
        Optional<Holiday> holiday = holidayRepository.findById(id);
        if(!holiday.isPresent()){
            throw new RuntimeException("Ngày lễ không tồn tại");
        }
        else{
            holidayRepository.deleteById(id);
            return new MessageResponse("Xóa Thành Công");
        }

    }
}
