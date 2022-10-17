package com.vmg.scrum.excel;

import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.SignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class DataExcelCalculation {
    @Autowired
    SignRepository signRepository;
    @Autowired
    LogDetailRepository logDetailRepository;
    public void convertSign(List<LogDetail> logDetails) {
        for (LogDetail logDetail : logDetails) {
                Integer dayOfWeek = logDetail.getDate_log().getDay();
                Integer hourIn =0;
                Integer hourOut=0;
                Integer minuteIn =0;
                Integer minuteOut=0;
                Integer secondIn =0;
                Integer secondOut=0;
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
                System.out.println(logDetail.getDate_log() +"-"+ dayOfWeek);
                if(dayOfWeek==0 || dayOfWeek==6){
                    Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
                    LogDetail logDetail2 = logDetail1.get();
                    logDetail2.setSigns(signRepository.findByName(ESign.NT));
                    logDetailRepository.save(logDetail2);
                }
                if((hourIn==0 && hourOut==0 && (dayOfWeek==0 || dayOfWeek==6)) || (hourIn==null && hourOut<15 ) || (hourIn==10 && secondIn>0 && hourOut==null)){
                    Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
                    LogDetail logDetail2 = logDetail1.get();
                    logDetail2.setSigns(signRepository.findByName(ESign.KL));
                    logDetailRepository.save(logDetail2);
                }
//                if(hourOut!=null && hourIn!=null){
//                    if( (hourIn==null && hourOut<15 ) || (hourIn==10 && secondIn>0 && hourOut==null)){
//                        Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
//                        LogDetail logDetail2 = logDetail1.get();
//                        logDetail2.setSigns(signRepository.findByName(ESign.KL));
//                        logDetailRepository.save(logDetail2);
//                    }
//                    if((hourIn==10 && secondIn>0) && ((hourOut==17 &&minuteOut>30) || hourOut>17)){
//                        Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
//                        LogDetail logDetail2 = logDetail1.get();
//                        logDetail2.setSigns(signRepository.findByName(ESign.KL_H));
//                        logDetailRepository.save(logDetail2);
//                    }
//                    if((hourIn<8||(hourIn=8 && minuteIn<30))){
//                        Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
//                        LogDetail logDetail2 = logDetail1.get();
//                        logDetail2.setSigns(signRepository.findByName(ESign.H_KL));
//                        logDetailRepository.save(logDetail2);
//                    }
//
//                }
//            if((hourIn==null && hourOut==null && (dayOfWeek==0 || dayOfWeek==6))){
//                Optional<LogDetail> logDetail1 = logDetailRepository.findById(logDetail.getId());
//                LogDetail logDetail2 = logDetail1.get();
//                logDetail2.setSigns(signRepository.findByName(ESign.KL));
//                logDetailRepository.save(logDetail2);
//            }
//                if(dayOfWeek!=0 && dayOfWeek!=6 && hourIn==null && hourOut==null)
//                    logDetail.setSigns(new Sign(ESign.KL));
//                if(hourIn>10 || (hourIn==10 && secondIn>0))
//                    logDetail.setSigns(new Sign(ESign.M));
//                if(hourOut<15)
//                    logDetail.setSigns(new Sign(ESign.S));
//                if((hourIn>10 || (hourIn==10 && secondIn>0)) && hourOut<15)
//                    logDetail.setSigns(new Sign(ESign.M_S));

        }

    }

}
