package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogDetailServiceImpl  implements LogDetailService{

    @Autowired
    private LogDetailRepository logDetailRepository;

    @Override
    public List<LogDetail> getLogDetailsByDpD(String key, LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public List<LogDetail> getLogDetailsByD(LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public List<LogDetail> getLogDetailsByDp(String key, Pageable pageable) {
        return null;
    }

    @Override
    public List<LogDetail> getLogAllDetails(Pageable pageable) {
        return null;
    }

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
