package com.vmg.scrum.service.impl;

import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.excel.ExcelImporter;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.SignupRequest;
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
    public List<LogDetail> listLog(MultipartFile file) throws IOException {
        List<LogDetail> logDetailList = excelImporter.read(file.getInputStream());
        return dataExcelCalculation.convertSign(logDetailList);
    }
    public List<SignupRequest> listUser(MultipartFile file) throws IOException {
           return excelImporter.readUser(file.getInputStream());
    }

}
