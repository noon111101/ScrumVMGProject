package com.vmg.scrum.controller;


import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.excel.ExcelExporter;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.DepartmentRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    DepartmentRepository departmentRepository;


    @GetMapping("/export")
    public ResponseEntity exportToExcel(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam int month, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bang_Cham_Cong_" + currentDateTime + ".xlsx";

        response.setHeader(headerKey, headerValue);
        List<LogDetail> listLogs = new ArrayList<>();
        if(id==0){
            listLogs = logDetailRepository.findByMonthSortDate(month);
            ExcelExporter excelExporter = new ExcelExporter(listLogs,month,departmentRepository,userRepository,logDetailRepository);
            excelExporter.export(response);
        }
        else {
            listLogs = logDetailRepository.findByMonthAndDepartmentSortDate(id, month);
            ExcelExporter excelExporter = new ExcelExporter(listLogs, id,month,departmentRepository,userRepository,logDetailRepository);
            excelExporter.export(response);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/import")
    public List<LogDetail> uploadFileLog(@ModelAttribute("file") MultipartFile file) throws IOException {
        try{
            if (excelImporter.hasExcelFormat(file)) {
                return  fileService.listLog(file);
            }
            else throw new RuntimeException("File không đúng định dạng (phải có đuôi .xlsx)");

        } catch (IOException e) {
            throw new RuntimeException("Upload file không thành công ");
        }
    }
    @PostMapping("/import/user")
    public List<SignupRequest> uploadFileUser(@ModelAttribute("file") MultipartFile file) throws IOException {
        try{
            if (excelImporter.hasExcelFormat(file)) {
                return  fileService.listUser(file);
            }
            else throw new RuntimeException("File không đúng định dạng (phải có đuôi .xlsx)");

        } catch (IOException e) {
            throw new RuntimeException("Upload file không thành công ");
        }

    }

}


