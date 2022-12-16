package com.vmg.scrum.excel;

import com.vmg.scrum.model.ENoteCatergory;
import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.NoteLog;
import com.vmg.scrum.repository.HolidayRepository;
import com.vmg.scrum.repository.LogDetailRepository;
import com.vmg.scrum.repository.NoteCatergoryRepository;
import com.vmg.scrum.repository.SignRepository;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataExcelCalculation {
    @Autowired
    SignRepository signRepository;
    @Autowired
    LogDetailRepository logDetailRepository;
    @Autowired
    HolidayRepository holidayRepository;
    @Autowired
    NoteCatergoryRepository noteCatergoryRepository;
    public List<LogDetail> convertSign(List<LogDetail> logDetails) {
        for (LogDetail logDetail : logDetails) {
           Holiday holiday= holidayRepository.findCurrentDate(logDetail.getDateLog().toString());
            if(holiday!=null){
                logDetail.setSigns(signRepository.findByName(ESign.L));
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_HOLIDAY));
                noteLog.setContent(holiday.getHolidayName());
                noteLog.setLastSign(logDetail.getSigns());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.L));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                continue;
            }
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
            if(dayOfWeek.toString().equals("SUNDAY") || dayOfWeek.toString().equals("SATURDAY")){
                logDetail.setSigns(signRepository.findByName(ESign.NT));
                continue;
            }
                if(hourOut==null && hourIn==null){

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
