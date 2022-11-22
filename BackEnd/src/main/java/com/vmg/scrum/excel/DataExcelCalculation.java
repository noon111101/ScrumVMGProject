package com.vmg.scrum.excel;

import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.SignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class DataExcelCalculation {
    @Autowired
    SignRepository signRepository;
    @Autowired
    LogDetailRepository logDetailRepository;
    public List<LogDetail> convertSign(List<LogDetail> logDetails) {
        for (LogDetail logDetail : logDetails) {
                DayOfWeek dayOfWeek = logDetail.getDateLog().getDayOfWeek();
                Integer hourIn =null;
                Integer hourOut=null;
                Integer minuteIn =null;
                Integer minuteOut=null;
                Integer secondIn =null;
                Integer secondOut=null;
                if(logDetail.getTimeIn()!=null) {
                    hourIn = logDetail.getTimeIn().getHour();
                    minuteIn = logDetail.getTimeIn().getMinute();
                    secondIn = logDetail.getTimeIn().getSecond();
                    System.out.println("Hour in : " + hourIn);
                    System.out.println("Minute in : " + minuteIn);
                    System.out.println("Second in : " + secondIn);

                }
                if(logDetail.getTimeOut()!=null) {
                    hourOut = logDetail.getTimeOut().getHour();
                    minuteOut = logDetail.getTimeOut().getMinute();
                    secondOut = logDetail.getTimeOut().getSecond();

                    System.out.println("Hour out : " + hourOut);
                    System.out.println("Minute out : " + minuteOut);
                    System.out.println("Second out : " + secondOut);

                }
                System.out.println(logDetail.getDateLog() +"-"+ dayOfWeek);

                if(hourOut==null && hourIn==null){
                    if(dayOfWeek.toString().equals("SUNDAY") || dayOfWeek.toString().equals("SATURDAY")){
                        logDetail.setSigns(signRepository.findByName(ESign.NT));
                    }
                    if(!dayOfWeek.toString().equals("SUNDAY") && !dayOfWeek.toString().equals("SATURDAY")){
                        logDetail.setSigns(signRepository.findByName(ESign.KL));
                    }
                }
                if(hourOut!=null && hourIn!=null){
                    if( (hourIn==null && hourOut<15 ) || (hourIn==10 && minuteIn>0 && hourOut==null) || ((hourIn==10 && minuteIn>0)&&hourOut<15) ){
                        logDetail.setSigns(signRepository.findByName(ESign.KL));
                    }
                    if((hourIn>10 || hourIn==10 && minuteIn>0) && ((hourOut==15 &&minuteOut>0) || hourOut>15)){
                        logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                    }
                    if((hourIn<10||(hourIn==10 && minuteIn==0)) && (hourOut<15)){
                        logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                    }
                    if((hourIn<10||(hourIn==10 && minuteIn==0)) && ((hourOut==15 &&minuteOut>0) || hourOut>15)) {
                        logDetail.setSigns(signRepository.findByName(ESign.H));
                    }

                }
                if(hourIn==null && hourOut!=null){
                    if(hourOut<15){
                        logDetail.setSigns(signRepository.findByName(ESign.KL));
                    }
                    if((hourOut==15 &&minuteOut>0) || hourOut>15){
                        logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                    }
                }
            if(hourIn!=null && hourOut==null){
                if(hourIn>10 || hourIn==10 && minuteIn>0){
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if(hourIn<10){
                    logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                }
            }

        }
        return logDetails;
    }

}
