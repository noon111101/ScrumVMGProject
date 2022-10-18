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

}
