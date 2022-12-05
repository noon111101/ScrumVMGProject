package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.payload.response.UserLogDetail;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import com.vmg.scrum.repository.FurloughRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/furlough")
public class FurloughController {
    @Autowired
    FurloughService furloughService;

    @GetMapping("furloughByYear")
    Map<String, List<FurloughReport>> getAllFurloughByYear(@RequestParam Long year) {
        return furloughService.getAllFurloughByYear(year);
    }
}


