package com.vmg.scrum.controller;

import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.model.User;
import com.vmg.scrum.payload.request.HolidayRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {
    @Autowired
    HolidayService holidayService;

    @Autowired
    HolidayRepository holidayRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addHoliday(@Valid @ModelAttribute HolidayRequest holidayRequest) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(holidayService.addHoliday(holidayRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Holiday> updateHoliday(@PathVariable("id") long id, @ModelAttribute HolidayRequest holidayRequest) {
        holidayService.updateHoliday(id, holidayRequest);
        return new ResponseEntity<>(holidayRepository.findById(id).get(), HttpStatus.OK);
    }
}
