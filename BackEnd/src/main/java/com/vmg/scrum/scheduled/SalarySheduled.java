package com.vmg.scrum.scheduled;

import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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
    @Scheduled(cron = "0 00 18 * * *")
    public void convertSignFace(){
        List<Request> requests = requestRepository.findByStatusList(2L);
        List<LogDetail> logDetails= logDetailRepository.findByCurrentDay(LocalDate.now());
        Holiday holiday = holidayRepository.findCurrentDate(LocalDate.now().toString());
        for (LogDetail logDetail : logDetails) {
            if(logDetail.isRequestActive())
                continue;
            if(holiday!=null){
                logDetail.setSigns(signRepository.findByName(ESign.L));
                logDetail.setReason(holiday.getHolidayName());
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
            logDetailRepository.save(logDetail);
        }
        if(requests.size()>0){
            for(Request request : requests){
                List<LogDetail> logDetailList = new ArrayList<>();
                if(request.getDateFrom()==request.getDateTo()){
                    if(logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),request.getDateTo())!=null)
                    logDetailList.add(logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),request.getDateTo()));
                    else logDetailList.add(new LogDetail(request.getCreator(),request.getDateTo()));

                }
                if(request.getDateTo()!=request.getDateFrom()){
                    if(logDetailRepository.findByUserCodeAndDateRange(request.getCreator().getCode(),request.getDateTo(),request.getDateFrom()).size()!=0)
                    logDetailList = logDetailRepository.findByUserCodeAndDateRange(request.getCreator().getCode(),request.getDateTo(),request.getDateFrom());
                    else {
                        for(int i=0;i<= Period.between(request.getDateFrom(),request.getDateTo()).getDays();i++)
                            logDetailList.add(new LogDetail(request.getCreator(),request.getDateFrom().plusDays(i)));
                    }
                }
                if(request.getDateTo()==null && request.getDateFrom()==null){
                    if(logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),request.getDateForget())!=null)
                        logDetailList.add(logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),request.getDateForget()));
                    else logDetailList.add(new LogDetail(request.getCreator(),request.getDateForget()));
                }
                switch (request.getCategoryReason().getId().intValue()){
                    //Nghỉ phép
                    case 1:

                            if(request.getTimeStart().getHour()>=13){
                                if(logDetailList.get(0).getSigns()!=null) {
                                    if (logDetailList.get(0).getSigns().getName().toString().startsWith("KL"))
                                        logDetailList.get(0).setSigns(signRepository.findByName(ESign.KL_P));
                                    if (logDetailList.get(0).getSigns().getName().toString().startsWith("H"))
                                        logDetailList.get(0).setSigns(signRepository.findByName(ESign.H_P));
                                }else logDetailList.get(0).setSigns(signRepository.findByName(ESign.KL_P));

                            }
                            else
                                logDetailList.get(0).setSigns(signRepository.findByName(ESign.P));


                            if(request.getTimeEnd().getHour()>13)
                                logDetailList.get(logDetailList.size()-1).setSigns(signRepository.findByName(ESign.P));
                            else {
                                if( logDetailList.get(logDetailList.size()-1).getSigns()!=null){
                                    if(logDetailList.get(logDetailList.size()-1).getSigns().getName().toString().endsWith("KL"))
                                        logDetailList.get(logDetailList.size()-1).setSigns(signRepository.findByName(ESign.P_KL));
                                    if(logDetailList.get(logDetailList.size()-1).getSigns().getName().toString().endsWith("H"))
                                        logDetailList.get(logDetailList.size()-1).setSigns(signRepository.findByName(ESign.P_H));
                                }
                                else logDetailList.get(logDetailList.size()-1).setSigns(signRepository.findByName(ESign.P_KL));
                            }



                        for(int i =1; i<logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.P));
                        break;
                        // nghỉ ốm
                    case 2:
                        for(int i =0; i<=logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.Ô));
                        break;
                    //Nghỉ tiêu chuẩn
                    case 4:
                        for(int i =0; i<=logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.TC));
                        break;
                    //Nghỉ chế độ thai sản
                    case 5:
                        for(int i =0; i<=logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.CĐ));
                        break;
                    //Quên chấm công
                    case 6:
                        for(int i =0; i<=logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.H));
                        break;
                    //Work from home && Đi công tác
                    case 3:
                    case 7:
                    case 8:
                            if (request.getTimeStart().getHour() >= 13) {
                                if(logDetailList.get(0).getSigns()!=null) {
                                    if (logDetailList.get(0).getSigns().getName().toString().startsWith("KL"))
                                        logDetailList.get(0).setSigns(signRepository.findByName(ESign.KL_H));
                                    if (logDetailList.get(0).getSigns().getName().toString().startsWith("H"))
                                        logDetailList.get(0).setSigns(signRepository.findByName(ESign.H));
                                }
                                else logDetailList.get(0).setSigns(signRepository.findByName(ESign.KL_H));

                            } else
                                logDetailList.get(0).setSigns(signRepository.findByName(ESign.H));


                            if(request.getTimeEnd().getHour()>13)
                                logDetailList.get(logDetailList.size()-1).setSigns(signRepository.findByName(ESign.H));
                            else {
                                if(logDetailList.get(logDetailList.size()-1).getSigns()!=null) {
                                    if (logDetailList.get(logDetailList.size() - 1).getSigns().getName().toString().endsWith("KL"))
                                        logDetailList.get(logDetailList.size() - 1).setSigns(signRepository.findByName(ESign.H_KL));
                                    if (logDetailList.get(logDetailList.size() - 1).getSigns().getName().toString().endsWith("H"))
                                        logDetailList.get(logDetailList.size() - 1).setSigns(signRepository.findByName(ESign.H));
                                }
                                else logDetailList.get(logDetailList.size() - 1).setSigns(signRepository.findByName(ESign.H_KL));
                            }
                        for(int i =1; i<logDetailList.size()-1;i++)
                            logDetailList.get(i).setSigns(signRepository.findByName(ESign.H));
                        break;
                    default:
                        break;
                }
                for(LogDetail logDetail : logDetailList){
                    logDetail.setRequestActive(true);
                    logDetailRepository.save(logDetail);
                }
                request.setApproveStatus(approveSttRepository.findById(6));
            }
        }

        System.out.println("Chay ham tinh toan ki tu cham cong vao " + LocalDate.now());
    }
}
