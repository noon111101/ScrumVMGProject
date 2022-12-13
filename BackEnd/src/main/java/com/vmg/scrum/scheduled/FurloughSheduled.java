package com.vmg.scrum.scheduled;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.repository.*;
import com.vmg.scrum.service.impl.FurloughServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Scheduled(cron = "0 0 0 1 * *")
    public void calculateFurloughReport() {
        Long currentMonth = Long.valueOf(LocalDate.now().getMonthValue());
        Long currentYear = Long.valueOf(LocalDate.now().getYear());
        List<User> users = userRepository.findAll();
        for (User user : users){
            List<LogDetail> logDetailsCurrentMonth = logDetailRepository.findByMonthAndUserCodeSortDate(LocalDate.now().getMonthValue(),user.getCode());
            float used = 0;
            for(LogDetail logDetail : logDetailsCurrentMonth){
                if(logDetail.getSigns().getName().toString().contains("_") && logDetail.getSigns().getName().toString().contains("P"))
                    used= used + 0.5F;
                if(logDetail.getSigns().getName().toString().contains("CĐ") || logDetail.getSigns().getName().toString().contains("TC") || logDetail.getSigns().getName().toString().contains("P"))
                    used=used + 1F;
            }
            Furlough furlough = furloughRepository.findByYearAndUserIdAndMonthInYear(currentYear,user.getId(),currentMonth);
            if(furlough==null)
                furlough=new Furlough(currentMonth,currentYear,used,user,furloughService.calculateAvailableUsedTillMonth(currentMonth,currentYear,used,user));
            furloughRepository.save(furlough);
        }
    }
}

