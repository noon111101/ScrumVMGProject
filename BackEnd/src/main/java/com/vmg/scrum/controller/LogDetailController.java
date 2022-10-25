package com.vmg.scrum.controller;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogDetailController {
    @Autowired
    LogDetailRepository logDetailRepository;

    @Autowired
    private LogDetailService logDetailService;

    @GetMapping("logList")
    public ResponseEntity<Page<LogDetail>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("byUser")
    public ResponseEntity<Page<LogDetail>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size, @RequestParam Double code) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findByUserCode(pageable, code), HttpStatus.OK);
    }

    @GetMapping("allByUser")
    public ResponseEntity<List<LogDetail>> getByUser(@RequestParam Double code) {
        return new ResponseEntity<>(logDetailRepository.findByUserCode(code), HttpStatus.OK);
    }

    @GetMapping("byDepartment")
    public ResponseEntity<Page<LogDetail>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size, @RequestParam Long id) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findByUserDepartmentsId(pageable, id), HttpStatus.OK);
    }

    @GetMapping("byDate_Usercode")
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
            pageLogs = logDetailRepository.findByDate_UserCode(code, from1, to1, pageable);

        }
        else{
            pageLogs = logDetailRepository.findByUserCode(pageable, code);

        }
        return new ResponseEntity<>(pageLogs, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<LogDetail>> searchByDepartmentAndDate(@RequestParam(name = "key") long key,
                                                                     @RequestParam(name = "date") String date,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.parse(LocalDate.now().format(sdf), sdf);
            Pageable pageable = PageRequest.of(page, size);
        if (!date.isEmpty() && key == 0) {
            LocalDate dates = LocalDate.parse(date, sdf);
            return new ResponseEntity<>(logDetailRepository.findByDate(dates, pageable), HttpStatus.OK);
        }
        if (date.isEmpty()) {
            return new ResponseEntity<>(logDetailRepository.findDateandDepartment(key,now, pageable), HttpStatus.OK);
        }
        if (!date.isEmpty() && key != 0) {
            LocalDate dates = LocalDate.parse(date, sdf);
            return new ResponseEntity<>(logDetailRepository.findDateandDepartment(key, dates, pageable), HttpStatus.OK);
        }
        if ( date.isEmpty() && key == 0) {
            return new ResponseEntity<>(logDetailRepository.findByDate(now,pageable), HttpStatus.OK);
        }
            return null;
    }

    @GetMapping("byDate_Department")
    public ResponseEntity<Page<LogDetail>> getLogsByDate_Department(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "30") int size,
                                                                    @RequestParam long id,
                                                                    @RequestParam(name = "date", required = false) String date) throws ParseException {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pageable pageable = PageRequest.of(page, size);
        Page<LogDetail> pageLogs = null;
        LocalDate now = LocalDate.parse(LocalDate.now().format(sdf), sdf);
        if(date != null && date!=""){
            LocalDate date1 = LocalDate.parse(date, sdf);
            pageLogs = logDetailRepository.findByDate_DepartmentId(id , date1, pageable);
        }
        else if(id == 0) {
            LocalDate date1 = LocalDate.parse(date, sdf);
            pageLogs = logDetailRepository.findByDate(now, pageable);
        }
        else  {
            pageLogs = logDetailRepository.findByDate_DepartmentId(id , now, pageable);
            System.out.println(now);
        }
        return new ResponseEntity<>(pageLogs, HttpStatus.OK);
    }
}
