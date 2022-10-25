package com.vmg.scrum.controller;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
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
    @GetMapping("logList")
    public ResponseEntity<Page<LogDetail>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findAll(pageable), HttpStatus.OK);
    }
    @GetMapping("byUser")
    public ResponseEntity<Page<LogDetail>> getByUser(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size,@RequestParam Double code)
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
                                                     @RequestParam(defaultValue = "5") int size,@RequestParam Long id)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(logDetailRepository.findByUserDepartmentsId(pageable,id), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<LogDetail>> searchByDepartmentAndDate(@RequestParam(name = "key") String key,
                                                                     @RequestParam(name = "date") String date,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
        try {
        List<LogDetail> logDetails = new ArrayList<LogDetail>();
        if (key.isEmpty() && !date.isEmpty()) {
            String[] time = date.split("-");
            LocalDate dates = LocalDate.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
            System.out.println(dates.toString());
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(logDetailRepository.findByDate(dates, pageable), HttpStatus.OK);
        }

        else if (date.isEmpty() && !key.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(logDetailRepository.findByDepartment(Integer.parseInt(key), pageable), HttpStatus.OK);
        }
        else if (!key.isEmpty() && !date.isEmpty()) {
            String[] time = date.split("-");
            LocalDate dates = LocalDate.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
            System.out.println(dates.toString());
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(logDetailRepository.findDateandDepartment(Integer.parseInt(key), dates, pageable), HttpStatus.OK);
        }
        if (key.isEmpty() && date.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(logDetailRepository.findAll(pageable), HttpStatus.OK);
        }
        if (logDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
