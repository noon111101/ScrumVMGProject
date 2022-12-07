package com.vmg.scrum.controller;

import com.vmg.scrum.model.Holiday;

import com.vmg.scrum.payload.request.HolidayRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @GetMapping("/{id}")
    public ResponseEntity<Holiday> getHoliday(@PathVariable("id") long id) {
        return new ResponseEntity<>(holidayRepository.findById(id).get(), HttpStatus.OK);
    }
    
    @GetMapping("")
    public ResponseEntity<Page<Holiday>> getAllHolidays(@RequestParam(name="page", defaultValue = "0") int page,
                                                        @RequestParam(name="size",defaultValue = "12") int size,
                                                        @RequestParam(name="search", required = false) String search,
                                                        @RequestParam(name = "year", defaultValue = "0") int year){
        Pageable pageable = PageRequest.of(page, size);
        Page<Holiday> pageHolidays = null;
        if(search!= null && search!= ""){
            if(year!=0){
                pageHolidays = holidayRepository.findAllSearchAndYear(search,year, pageable);
            }else{
                pageHolidays = holidayRepository.findAllSearch(search, pageable);
            }
        }
        else {
            if(year!=0){
                pageHolidays = holidayRepository.findAllHolidaysByYear(year, pageable);
            }else{
                pageHolidays = holidayRepository.findAllHolidays(pageable);
            }

        }
        return new ResponseEntity<>(pageHolidays, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<MessageResponse> getAllHolidays(@RequestParam long id){
        return ResponseEntity.ok(holidayService.deleteHoliday(id));
    }
}
