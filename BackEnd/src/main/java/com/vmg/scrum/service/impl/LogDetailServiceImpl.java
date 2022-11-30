package com.vmg.scrum.service.impl;

import com.vmg.scrum.exception.custom.UpdateNullException;
import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.request.FaceKeepRequest;
import com.vmg.scrum.payload.request.ImageLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.SignRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


@Service
public class LogDetailServiceImpl  implements LogDetailService{

    @Autowired
    private LogDetailRepository logDetailRepository;
    @Autowired
    SignRepository signRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileManagerService fileManagerService;
    @Override
    public MessageResponse updateLogDetails(EditLogRequest[] editLogRequest) {

            if(editLogRequest.length==0){
                throw  new UpdateNullException("Chưa có chỉnh sửa nào");
            }
            for(EditLogRequest editLogRequest1 : editLogRequest){
                String[] time = editLogRequest1.getDate().split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
                System.out.println(date.toString());
                LogDetail logDetail = new LogDetail() ;
                if(logDetailRepository.findByUserCodeAndDate(editLogRequest1.getCode(), date)!=null){
                    logDetail=logDetailRepository.findByUserCodeAndDate(editLogRequest1.getCode(), date);
                    if(editLogRequest1.getSign()==null)
                        logDetail.setSigns(null);
                    else
                    logDetail.setSigns(signRepository.findByName(ESign.valueOf(editLogRequest1.getSign())));
                    logDetail.setReason(editLogRequest1.getReason());
                }
                else {
                    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    logDetail.setUser(userRepository.findByCode(editLogRequest1.getCode()));
                    logDetail.setDateLog(LocalDate.parse(editLogRequest1.getDate(),sdf));
                    if(editLogRequest1.getSign()==null)
                        logDetail.setSigns(null);
                    else
                    logDetail.setSigns(signRepository.findByName(ESign.valueOf(editLogRequest1.getSign())));
                    logDetail.setReason(editLogRequest1.getReason());
                }

                logDetailRepository.save(logDetail);
            }
            return new MessageResponse("Sign updated successfully!");
    }

    @Override
    public String sendImg(ImageLogRequest imageLogRequest) {
        return fileManagerService.saveLog(imageLogRequest);
    }

    @Override
    public LogDetail faceTimeKeep(FaceKeepRequest faceKeepRequest) {
        System.out.println(LocalDate.now());
        LogDetail logDetail = logDetailRepository.findByUserCodeAndDate(faceKeepRequest.getCode(),LocalDate.now());
        System.out.println(logDetail);
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter sdfdate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String time = faceKeepRequest.getTime().split(" ")[1];
        LocalTime timeParse = LocalTime.parse(time,sdf);
        String date = faceKeepRequest.getTime().split(" ")[0];
        LocalDate dateParse = LocalDate.parse(date,sdfdate);
        if(logDetail!=null){
            logDetail.setTimeOut(timeParse);
        }
        else {
            logDetail = new LogDetail();
            logDetail.setUser(userRepository.findByCode(faceKeepRequest.getCode()));
            logDetail.setDateLog(dateParse);
            logDetail.setTimeIn(timeParse);
        }
        logDetailRepository.save(logDetail);
        return logDetail;
    }

}
