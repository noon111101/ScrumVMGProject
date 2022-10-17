package com.vmg.scrum.controller;


import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.excel.ExcelExporter;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.impl.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ExcelImporter excelImporter;

    @Autowired
    ExcelService fileService;
    @Autowired
    DataExcelCalculation dataExcelCalculation;
    @Autowired
    LogDetailRepository logDetailRepository;
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
    @PostMapping("/import")
    public ResponseEntity<MessageResponse> uploadFile(@ModelAttribute("file") MultipartFile file) {
        String message = "";

        if (excelImporter.hasExcelFormat(file)) {
            try {
                fileService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @GetMapping("/test")
    public void test(){
        dataExcelCalculation.convertSign(logDetailRepository.findAll());
    }

}


