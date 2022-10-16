package com.vmg.scrum.controller;


import com.vmg.scrum.excel.ExcelExporter;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
//@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ExcelImporter excelImporter;
    @GetMapping("/export")
    public ResponseEntity exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Employees_" + currentDateTime + ".xlsx";

        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userRepository.findAll();

        ExcelExporter excelExporter = new ExcelExporter(listUsers);

        excelExporter.export(response);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/import")
    public List<LogDetail> importToExcel() throws IOException {
        return  excelImporter.read();
    }
}
