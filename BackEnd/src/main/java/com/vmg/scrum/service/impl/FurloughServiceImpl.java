package com.vmg.scrum.service.impl;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.request.EditFurloughRequest;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.pojo.FurloughCurrentEdit;
import com.vmg.scrum.pojo.FurloughPreviousEdit;
import com.vmg.scrum.pojo.MonthFurloughEdit;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import com.vmg.scrum.repository.FurloughRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.FurloughService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FurloughServiceImpl implements FurloughService {
    @Autowired
    FurloughRepository furloughRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    FurloughHistoryRepository furloughHistoryRepository;
    @Override
    public Map<String, List<FurloughReport>> getAllFurloughByYear(Long year,String departName) {
            List<Department> departments = departmentRepository.findAll();
            if(departName!=""){
                List<Department> departmentList= new ArrayList<>();
                departmentList.add(departmentRepository.findByName(departName));
                departments=departmentList;
            }
            Map<String, List<FurloughReport>> departmentListMap = new HashMap<>();
            for (Department department : departments) {
                List<FurloughReport> furloughReports = new ArrayList<>();
                List<User> users = userRepository.findAllByDepartments_Id(department.getId());
                if (users.size() == 0)
                    continue;
                else {
                    for (User user : users) {
                        List<Furlough> furloughs = furloughRepository.findByYearAndUserId(year, user.getId());
                        List<Furlough> furloughList = new ArrayList<>();
                        FurloughHistory furloughHistory = furloughHistoryRepository.findByYearAndUserId(year, user.getId());
                        if (furloughHistory == null) {
                            furloughHistory = new FurloughHistory();
                            furloughHistory.setAvailibleCurrentYear(12);
                        }
                        for (int i = 1; i <= 12; i++) {
                            Furlough furlough1 = new Furlough();
                            furlough1.setMonthInYear((long) i);
                            furlough1.setYear(year);
                            furlough1.setUsedInMonth((float) 0);
                            if (furloughs.size() > 0) {
                                boolean check = true;
                                for (Furlough furlough : furloughs) {
                                    if (furlough.getMonthInYear() == i) {
                                        furloughList.add(furlough);
                                        check = false;
                                        break;
                                    }
                                }
                                if (check)
                                    furloughList.add(furlough1);
                            } else furloughList.add(furlough1);
                        }
                        FurloughReport furloughReport = new FurloughReport(user, furloughList, year, furloughHistory);
                        furloughReports.add(furloughReport);
                    }
                }
                departmentListMap.put(department.getName(), furloughReports);
            }

        return departmentListMap;

    }

    @Override
    public MessageResponse editFurloughReport(EditFurloughRequest editFurloughRequest) {
        try{
            if (editFurloughRequest.getMonthFurloughEdits().size() != 0) {
                List<MonthFurloughEdit> monthFurloughEdits = editFurloughRequest.getMonthFurloughEdits();
                for(MonthFurloughEdit monthFurloughEdit : monthFurloughEdits){
                    Furlough furloughEdit = furloughRepository.findByYearAndUserIdAndMonthInYear(editFurloughRequest.getYear(),monthFurloughEdit.getId(),monthFurloughEdit.getMonth());
                    furloughEdit.setUsedInMonth(monthFurloughEdit.getUsed());
                    furloughRepository.save(furloughEdit);
                }
            }
            if (editFurloughRequest.getFurloughCurrentEdits().size() != 0) {
                List<FurloughCurrentEdit> furloughCurrentEdits = editFurloughRequest.getFurloughCurrentEdits();
                for(FurloughCurrentEdit furloughCurrentEdit : furloughCurrentEdits){
                    FurloughHistory furloughEdit = furloughHistoryRepository.findByYearAndUserId(editFurloughRequest.getYear(),furloughCurrentEdit.getId());
                    furloughEdit.setAvailibleCurrentYear(furloughCurrentEdit.getAvailibleCurrentYear());
                    furloughHistoryRepository.save(furloughEdit);
                }
            }
            if (editFurloughRequest.getFurloughPreviousEdits().size() != 0) {
                List<FurloughPreviousEdit> furloughPreviousEdits = editFurloughRequest.getFurloughPreviousEdits();
                for(FurloughPreviousEdit furloughPreviousEdit : furloughPreviousEdits){
                    FurloughHistory furloughEdit = furloughHistoryRepository.findByYearAndUserId(editFurloughRequest.getYear(),furloughPreviousEdit.getId());
                    furloughEdit.setLeftFurlough(furloughPreviousEdit.getOddCurrentYear());
                    furloughHistoryRepository.save(furloughEdit);
                }
            }
            return new MessageResponse("Chỉnh sửa thống kê nghỉ phép thành công");
        }catch (Exception e){
            throw  new RuntimeException("Lỗi hệ thống ");
        }
    }
}

