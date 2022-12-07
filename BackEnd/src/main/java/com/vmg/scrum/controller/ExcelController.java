package com.vmg.scrum.controller;


import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.excel.ExcelExportPhep;
import com.vmg.scrum.excel.ExcelExporterReport;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import com.vmg.scrum.service.impl.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    FurloughService furloughService;
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


    @GetMapping("/export_report")
    public ResponseEntity exportToExcel(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam int month, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bang Cham Cong " + currentDateTime + ".xlsx";

        response.setHeader(headerKey, headerValue);
        List<LogDetail> listLogs = new ArrayList<>();
        if(id==0){
            listLogs = logDetailRepository.findByMonthSortDate(month);
            ExcelExporterReport excelExporter = new ExcelExporterReport(listLogs,month,departmentRepository,userRepository,logDetailRepository);
            excelExporter.export(response);
        }
        else {
            listLogs = logDetailRepository.findByMonthAndDepartmentSortDate(id, month);
            ExcelExporterReport excelExporter = new ExcelExporterReport(listLogs, id,month,departmentRepository,userRepository,logDetailRepository);
            excelExporter.export(response);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/export_phep")
    public ResponseEntity exportPhep(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam Long year, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bang Ngay Phep " + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        if(id==0){
//            listLogs = logDetailRepository.findByMonthSortDate(month);
            ExcelExportPhep excelExporter = new ExcelExportPhep(year, departmentRepository, userRepository, furloughService);
            excelExporter.export(response);
        }
        else {
//            listLogs = logDetailRepository.findByMonthAndDepartmentSortDate(id, month);
            ExcelExportPhep excelExporter = new ExcelExportPhep(year, departmentRepository,userRepository, furloughService);
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


