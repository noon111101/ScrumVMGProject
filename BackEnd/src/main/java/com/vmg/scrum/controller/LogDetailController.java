package com.vmg.scrum.controller;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/log")
public class LogDetailController {
    @Autowired
    LogDetailRepository logDetailRepository;
    @GetMapping("logList")
    public ResponseEntity<Page<LogDetail>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findAll(pageable), HttpStatus.OK);
    }
    @GetMapping("byUser")
    public ResponseEntity<Page<LogDetail>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam Double code)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findByUserCode(pageable,code), HttpStatus.OK);
    }

    @GetMapping("byDate")
    public ResponseEntity<Page<LogDetail>> getLogsByDate_UserCode(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "30") int size,
                                                     @RequestParam Double code,
                                                     @RequestParam(name = "from", required = false) String from,
                                                     @RequestParam(name = "to", required = false) String to) throws ParseException {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pageable pageable = PageRequest.of(page, size);
        Page<LogDetail> pageLogs = null;
        if(from != null && to!=null && from != "" && to!=""){
            LocalDate from1 = LocalDate.parse(from, sdf);
            LocalDate to1 = LocalDate.parse(to, sdf);
            pageLogs = logDetailRepository.findByDate(code, from1, to1, pageable);

        }
        else{
            pageLogs = logDetailRepository.findByUserCode(pageable, code);

        }
        return new ResponseEntity<>(pageLogs, HttpStatus.OK);
    }
    @GetMapping("allByUser")
    public ResponseEntity<List<LogDetail>> getByUser(@RequestParam Double code)
    {
        return new ResponseEntity<>(logDetailRepository.findByUserCode(code), HttpStatus.OK);
    }
    @GetMapping("byDepartment")
    public ResponseEntity<Page<LogDetail>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam Long id)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findByUserDepartmentsId(pageable,id), HttpStatus.OK);
    }

}
