package com.vmg.scrum.scheduled;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.repository.*;
import com.vmg.scrum.service.impl.FurloughServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class FurloughSheduled {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FurloughRepository furloughRepository;
    @Autowired
    FurloughServiceImpl furloughService;
    @Autowired
    LogDetailRepository logDetailRepository;
    @Autowired
    FurloughHistoryRepository furloughHistoryRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    public void calculateFurloughReport() {
        Long currentMonth = Long.valueOf(LocalDate.now().getMonthValue());
        Long currentYear = Long.valueOf(LocalDate.now().getYear());
        List<User> users = userRepository.findAll();
        for (User user : users){
            List<LogDetail> logDetailsCurrentMonth = logDetailRepository.findByMonthAndYearAndUserCodeSortDate(LocalDate.now().getMonthValue(),LocalDate.now().getYear(), user.getCode());
            float used = 0;
            for(LogDetail logDetail : logDetailsCurrentMonth){
                if(logDetail.getSigns().getName().toString().contains("_") && logDetail.getSigns().getName().toString().contains("P"))
                    used= used + 0.5F;
                if(logDetail.getSigns().getName().toString().contains("P") && !logDetail.getSigns().getName().toString().contains("_"))
                    used=used + 1F;
            }
            Furlough furlough = furloughRepository.findByYearAndUserIdAndMonthInYear(currentYear,user.getId(),currentMonth);
            furlough.setUsedInMonth(used);
            if(furlough==null)
                furlough=new Furlough(currentMonth,currentYear,used,user,furloughService.calculateAvailableUsedTillMonth(currentMonth,currentYear,user));
            furloughRepository.save(furlough);
            if(currentMonth<12){
                Furlough furlough1 = new Furlough(currentMonth + 1 ,currentYear,0F,user,furloughService.calculateAvailableUsedTillMonth(currentMonth,currentYear,user));
                furloughRepository.save(furlough1);
            }
            if(currentMonth==12){
                FurloughHistory furloughHistory = new FurloughHistory();
                float availibleCurrentYear = 12;
                if (user.getStartWork().getYear() == LocalDate.now().getYear() ) {
                    if (user.getStartWork().getDayOfMonth() >= 15)
                        availibleCurrentYear = availibleCurrentYear - user.getStartWork().getMonthValue();
                    else availibleCurrentYear = availibleCurrentYear - user.getStartWork().getMonthValue() + 1;
                }
                availibleCurrentYear = availibleCurrentYear + (Period.between(user.getStartWork(),LocalDate.now()).getDays()/365);
                //Ngày giờ nghỉ làm
                if(user.getEndWork()!=null){
                    int leftMonth = user.getEndWork().getMonthValue();
                    int leftYear = user.getEndWork().getYear();
                    int leftDate = user.getEndWork().getDayOfMonth();
                    if(leftYear==LocalDate.now().getYear()){
                        if(leftDate>=15)
                            availibleCurrentYear= 12-(12-leftMonth);
                        else availibleCurrentYear=12-(12-leftMonth)+1;
                    } else if(leftYear<LocalDate.now().getYear()) availibleCurrentYear=0;
                }
                furloughHistory.setAvailibleCurrentYear(availibleCurrentYear);
                furloughHistory.setUser(user);
                furloughHistory.setYear(currentYear+1);
                furloughHistory.setLeftFurlough(furloughService.calculateAvailableUsedTillMonth(currentMonth,currentYear,user)-furlough.getUsedInMonth());
                furloughHistoryRepository.save(furloughHistory);
                Furlough furlough1=new Furlough(1L,currentYear+1,0F,user,furloughService.calculateAvailableUsedTillMonth(1L,currentYear+1,user));
                furloughRepository.save(furlough1);
            }
        }
    }
}

