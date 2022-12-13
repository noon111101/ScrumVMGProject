package com.vmg.scrum.controller;

import com.vmg.scrum.excel.FurloughImporter;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.request.EditFurloughRequest;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.payload.response.UserLogDetail;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import com.vmg.scrum.repository.FurloughRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import com.vmg.scrum.service.impl.FurloughServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    FurloughRepository furloughRepository;
    @Autowired
    FurloughHistoryRepository furloughHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FurloughServiceImpl furloughServiceImpl;
    @Autowired
    FurloughImporter furloughImporter;

    @GetMapping("furloughByYear")
    Map<String, List<FurloughReport>> getAllFurloughByYear(@RequestParam Long year,@RequestParam String department) {
        return furloughService.getAllFurloughByYear(year,department);
    }
    @PostMapping("edit")
    MessageResponse editFurloughReport(@RequestBody EditFurloughRequest editFurloughRequest) {
        return furloughService.editFurloughReport(editFurloughRequest);
    }
    @PostMapping("create")
    MessageResponse create(@RequestParam Long month,@RequestParam Long year,@RequestParam Float used) {
        try {
            Furlough furlough = new Furlough(month, year, used,userRepository.findByCode("VMG_1111"),
                    furloughServiceImpl.calculateAvailableUsedTillMonth(month,year,userRepository.findByCode("VMG_1111")));
            furloughRepository.save(furlough);
        }catch (Exception e){
            return new MessageResponse("That bai");
        }
        return new MessageResponse("Thanh cong");

    }
    @PostMapping("import")
    public List<Furlough> importFurlough(@ModelAttribute("file") MultipartFile file,@RequestParam Long year) throws IOException {
        try{
            if (furloughImporter.hasExcelFormat(file)) {
                return  furloughImporter.readFurlough(file.getInputStream(),year);
            }
            else throw new RuntimeException("File không đúng định dạng (phải có đuôi .xlsx)");

        } catch (IOException e) {
            throw new RuntimeException("Upload file không thành công ");
        }
    }
}


