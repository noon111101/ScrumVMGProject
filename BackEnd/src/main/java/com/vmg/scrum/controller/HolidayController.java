package com.vmg.scrum.controller;

import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {
    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    HolidayService holidayService;

    @GetMapping("")
    public ResponseEntity<Page<Holiday>> getAllHolidays(@RequestParam(name="page", defaultValue = "0") int page,
                                                        @RequestParam(name="size",defaultValue = "12") int size,
                                                        @RequestParam(name="search", required = false) String search){
        Pageable pageable = PageRequest.of(page, size);
        Page<Holiday> pageHolidays = null;
        if(search!= null && search!= ""){
            pageHolidays = holidayRepository.findAllSearch(search, pageable);
        }
        else {
            pageHolidays = holidayRepository.findAll(pageable);
        }
        return new ResponseEntity<>(pageHolidays, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<MessageResponse> getAllHolidays(@RequestParam long id){
        return ResponseEntity.ok(holidayService.deleteHoliday(id));
    }

}
