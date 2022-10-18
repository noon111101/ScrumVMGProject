package com.vmg.scrum.service.impl;

import com.vmg.scrum.dto.LogDetailDTO;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LogDetailServiceImpl  implements LogDetailService{

//    @Autowired
//    LogDetailRepository logDetailRepository;
//
//    @Override
//    public Page<LogDetailDTO> getLogs(Pageable pageable, int page, int size) {
//        List<LogDetail> logs = logDetailRepository.getLogs();
//        List<LogDetailDTO> logDetails = new ArrayList<>();
//        for (int i=0;i<logs.size();i++) {
//            LogDetailDTO logDetailDTO = new LogDetailDTO();
//            DateFormat sdf1 = new SimpleDateFormat("dd-MM-YYYY");
//            DateFormat  sdf2 = new SimpleDateFormat("HH:mm:ss");
//            String date_log = sdf1.format(logs.get(i).getDate_log());
//            System.out.println(date_log);
//            String timeIn = sdf2.format(logs.get(i).getTimeIn());
//            System.out.println(timeIn);
//            String timeOut = sdf2.format(logs.get(i).getTimeOut());
//            logDetailDTO.setDate_log(date_log);
//            logDetailDTO.setTimeOut(timeOut);
//            logDetailDTO.setTimeIn(timeIn);
//
//            logDetails.add(logDetailDTO);
//
//        }
//        pageable = PageRequest.of(page, size);
//        Page<LogDetailDTO> pages = new PageImpl<>(logDetails, pageable, logs.size());
//        return pages;
//    }
}
