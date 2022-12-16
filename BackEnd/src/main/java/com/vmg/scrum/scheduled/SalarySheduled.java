package com.vmg.scrum.scheduled;

import com.vmg.scrum.model.ENoteCatergory;
import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.NoteLog;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class SalarySheduled {
    @Autowired
    SignRepository signRepository;
    @Autowired
    LogDetailRepository logDetailRepository;
    @Autowired
    HolidayRepository holidayRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ApproveSttRepository approveSttRepository;
    @Autowired
    NoteCatergoryRepository noteCatergoryRepository;
    @Autowired
    UserRepository userRepository;
    @Scheduled(cron = "0 00 18 * * *")
    public void convertSignFace(){
        List<Request> requests = requestRepository.findByStatusAndDateList(2,LocalDate.now());
        List<LogDetail> logDetails= logDetailRepository.findByCurrentDay(LocalDate.now());
        Holiday holiday = holidayRepository.findCurrentDate(LocalDate.now().toString());
        for (LogDetail logDetail : logDetails) {
            Sign currentSign = logDetail.getSigns();
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
                logDetailRepository.save(logDetail);
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
            Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
            if (noteCatergorySet == null)
                noteCatergorySet = new HashSet<>();
            NoteLog noteLog = new NoteLog();
            noteLog.setLogDetail(logDetail);
            noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_FACE));
            noteLog.setLastSign(currentSign);
            noteLog.setCreateDate(LocalDateTime.now());
            noteLog.setSignChange(logDetail.getSigns());
            noteCatergorySet.add(noteLog);
            logDetail.setNoteLogSet(noteCatergorySet);
            logDetailRepository.save(logDetail);
        }
        if(requests.size()>0){
            for(Request request : requests){
                LogDetail logDetail = logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),LocalDate.now());
                Sign currentSign = logDetail.getSigns();
                LocalDate currentDay = LocalDate.now();
                switch (request.getCategoryReason().getId().intValue()){
                    //Nghỉ phép
                    case 1:
                            if(request.getDateFrom().equals(currentDay)){
                                if(request.getTimeStart().getHour()>13 || (request.getTimeStart().getHour()==13 && request.getTimeStart().getMinute()>0 )){
                                    if(logDetail.getSigns()!=null) {
                                        if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                            logDetail.setSigns(signRepository.findByName(ESign.KL_P));
                                        if (logDetail.getSigns().getName().toString().startsWith("H"))
                                            logDetail.setSigns(signRepository.findByName(ESign.H_P));
                                    }else logDetail.setSigns(signRepository.findByName(ESign.KL_P));

                                }
                                else
                                    logDetail.setSigns(signRepository.findByName(ESign.P));
                            }
                            if(request.getDateTo().equals(currentDay)){
                                if(request.getTimeEnd().getHour()>13 || (request.getTimeEnd().getHour()==13 && request.getTimeEnd().getMinute()>0))
                                    logDetail.setSigns(signRepository.findByName(ESign.P));
                                else {
                                    if( logDetail.getSigns()!=null){
                                        if(logDetail.getSigns().getName().toString().endsWith("KL"))
                                            logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                        if(logDetail.getSigns().getName().toString().endsWith("H"))
                                            logDetail.setSigns(signRepository.findByName(ESign.P_H));
                                    }
                                    else logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                }
                            }
                        else if(currentDay!=request.getDateTo() && currentDay!= request.getDateFrom()) logDetail.setSigns(signRepository.findByName(ESign.P));
                        break;
                        // nghỉ ốm
                    case 3:
                        logDetail.setSigns(signRepository.findByName(ESign.Ô));
                        break;
                    //Nghỉ tiêu chuẩn
                    case 4:
                        logDetail.setSigns(signRepository.findByName(ESign.TC));
                        break;
                    //Nghỉ chế độ thai sản
                    case 5:
                        logDetail.setSigns(signRepository.findByName(ESign.CĐ));
                        break;
                    //Quên chấm công
                    case 6:
                        logDetail.setSigns(signRepository.findByName(ESign.H));
                        break;
                    //Work from home && Đi công tác
                    case 7:
                    case 8:
                        if(request.getDateFrom().equals(currentDay)){
                                if (request.getTimeStart().getHour() > 13 || (request.getTimeStart().getHour() == 13 && request.getTimeStart().getMinute() > 0)) {
                                    if (logDetail.getSigns() != null) {
                                        if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                            logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                                        if (logDetail.getSigns().getName().toString().startsWith("H"))
                                            logDetail.setSigns(signRepository.findByName(ESign.H));
                                    } else logDetail.setSigns(signRepository.findByName(ESign.KL_H));

                                } else
                                    logDetail.setSigns(signRepository.findByName(ESign.H));
                            }

                        if(request.getDateTo().equals(currentDay)){
                                    if (request.getTimeEnd().getHour() > 13 || (request.getTimeEnd().getHour() == 13 && request.getTimeEnd().getMinute() > 0))
                                        logDetail.setSigns(signRepository.findByName(ESign.H));
                                    else {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().endsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                            if (logDetail.getSigns().getName().toString().endsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                    }
                                }
                        else if(currentDay!=request.getDateTo() && currentDay!= request.getDateFrom()) logDetail.setSigns(signRepository.findByName(ESign.H));
                        break;
                    default:
                        break;
                }
                logDetail.setRequestActive(true);
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                noteLog.setApprovers(request.getApprovers());
                noteLog.setContent(request.getContent());
                noteLog.setLastSign(currentSign);
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(logDetail.getSigns());
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetailRepository.save(logDetail);
            }
        }
        System.out.println("Chay ham tinh toan ki tu cham cong vao " + LocalDate.now());
    }
}
