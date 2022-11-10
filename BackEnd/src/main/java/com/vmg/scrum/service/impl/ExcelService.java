package com.vmg.scrum.service.impl;

import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.LogDetailTotalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    LogDetailRepository logDetailRepository;
    @Autowired
    LogDetailTotalRepository logDetailTotalRepository;
    @Autowired
    ExcelImporter excelImporter;
    @Autowired
    DataExcelCalculation dataExcelCalculation;
    public void save(MultipartFile file) throws IOException {
        List<LogDetail> logDetailList = excelImporter.read(file.getInputStream());
        logDetailRepository.saveAll(dataExcelCalculation.convertSign(logDetailList));
    }
    public void saveUser(MultipartFile file) throws IOException {
            List<User> userList = excelImporter.readUser(file.getInputStream());
    }

    public List<LogDetail> getAllLog() {
        return logDetailRepository.findAll();
    }

}
