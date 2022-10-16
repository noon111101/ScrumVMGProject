package com.vmg.scrum.service.impl;

import com.vmg.scrum.excel.ExcelImporter;
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
    private ExcelImporter excelImporter;
    public void save(MultipartFile file) {
        try {
            List<LogDetail> logDetailList = excelImporter.read(file.getInputStream());
            logDetailRepository.saveAll(logDetailList);
//            logDetailTotalRepository.saveAll(excelImporter.logDetailTotals);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<LogDetail> getAllLog() {
        return logDetailRepository.findAll();
    }

}
