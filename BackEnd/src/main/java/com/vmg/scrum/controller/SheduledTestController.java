package com.vmg.scrum.controller;

import com.vmg.scrum.scheduled.FurloughSheduled;
import com.vmg.scrum.scheduled.SalarySheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduled")
public class SheduledTestController {
    @Autowired
    SalarySheduled salarySheduled;
    @Autowired
    FurloughSheduled furloughSheduled;

    @GetMapping("furlough")
    public void setFurloughSheduled(){
        furloughSheduled.calculateFurloughReport();
    }
    @GetMapping("salary")
    public void setSalarySheduledSheduled(){
        salarySheduled.convertSignFace();
    }

}
