package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.SignRepository;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogDetailServiceImpl  implements LogDetailService{

    @Autowired
    private LogDetailRepository logDetailRepository;
    @Autowired
    SignRepository signRepository;

    @Override
    public MessageResponse updateLogDetails(EditLogRequest[] editLogRequest) {
        try {
            for(EditLogRequest editLogRequest1 : editLogRequest){
                String[] time = editLogRequest1.getDate().split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
                System.out.println(date.toString());
                LogDetail logDetail = logDetailRepository.findByUserCodeAndDate(editLogRequest1.getCode(), date);
                logDetail.setSigns(signRepository.findByName(ESign.valueOf(editLogRequest1.getSign())));
                logDetailRepository.save(logDetail);
            }
            return new MessageResponse("Sign updated successfully!");
        } catch (Exception e){
            return new MessageResponse("Sign update fail!");
        }

    }

}
