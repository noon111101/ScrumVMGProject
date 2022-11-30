package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.Holiday;

import com.vmg.scrum.payload.request.HolidayRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.service.HolidayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public MessageResponse addHoliday(HolidayRequest holidayRequest) throws MessagingException, UnsupportedEncodingException {

            if (holidayRepository.existsByDateFromAndDateTo(holidayRequest.getDateFrom(), holidayRequest.getDateTo())){
                throw new RuntimeException("Ngày nghỉ lễ đã tồn tại");
        }

        if (holidayRequest.getDateFrom().isAfter(holidayRequest.getDateTo())) {
            throw new RuntimeException("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
        }
        Holiday holiday = new Holiday(holidayRequest.getName(), holidayRequest.getDateFrom(), holidayRequest.getDateTo());
        holidayRepository.save(holiday);
        return new MessageResponse("Thêm ngày nghỉ thành công!");
    }

    @Override
    public void updateHoliday(long id, HolidayRequest holidayRequest) {

        Holiday holiday = holidayRepository.findById(id).get();

        if (!holiday.getDateFrom().equals(holidayRequest.getDateFrom()) || !holiday.getDateTo().equals(holidayRequest.getDateTo()) ) {
            if (holidayRepository.existsByDateFromAndDateTo(holidayRequest.getDateFrom(), holidayRequest.getDateTo()))
                throw new RuntimeException("Ngày nghỉ lễ đã tồn tại");
        }

        if (holidayRequest.getDateFrom().isAfter(holidayRequest.getDateTo())) {
            throw new RuntimeException("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
        }
        holiday.setHolidayName(holidayRequest.getName());
        holiday.setDateFrom(holidayRequest.getDateFrom());
        holiday.setDateTo(holidayRequest.getDateTo());
        holidayRepository.save(holiday);

    }

    @Override
    public MessageResponse deleteHoliday(Long id) {
        Optional<Holiday> holiday = holidayRepository.findById(id);
        if (!holiday.isPresent()) {
            throw new RuntimeException("Ngày lễ không tồn tại");
        } else {
            holidayRepository.deleteById(id);
            return new MessageResponse("Xóa Thành Công");
        }
    }

    @Override
    public Page<Holiday> pageHolidays(String search, Pageable pageable) {
        Page<Holiday> pageHolidays = null;
        if(search!= null && search!= ""){
            pageHolidays = holidayRepository.findAllSearch(search, pageable);
        }
        else {
            pageHolidays = holidayRepository.findAllHolidays(pageable);
        }
        return pageHolidays;
    }


}
