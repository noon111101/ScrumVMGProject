package com.vmg.scrum.payload.response;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;


public class FurloughReport {
    @Autowired
    FurloughHistoryRepository furloughHistoryRepository;
    private User user;

    private List<Furlough> furloughs;

    //số ngày được nghỉ trong năm hiện tại
    private float availibleCurrentYear;

    //số ngày phép dư đầu kì
    private float oddCurrentYear;

    //Tổng số ngày phép đã nghỉ trước tháng 4
    private float usedBeforeApril;

    //Tổng số ngày nghỉ phép năm hiện tại
    private float usedInYear;

    //Số ngày phép còn lại năm trước
    private float leftLastYear;

    //Số ngày phép còn lại năm hiện tại
    private float leftCurentYear;

    //Trả phép
    private float payFurlough;

    //Số ngày phép được sử dụng đến tháng hiện tại
    private float availibleUsePresentMonth;

    private boolean probation;

    public boolean isLeaveCurrentYear() {
        return leaveCurrentYear;
    }

    public void setLeaveCurrentYear(boolean leaveCurrentYear) {
        this.leaveCurrentYear = leaveCurrentYear;
    }

    private boolean leaveCurrentYear;


    public FurloughReport(User user, List<Furlough> furloughs,Long year,FurloughHistory furloughHistory) {
        LocalDate startWork = user.getStartWork();
        this.user = user;
        this.furloughs = furloughs;
        this.probation=false;
        this.leaveCurrentYear=false;
        if(user.getEndWork()!=null){
            if(user.getEndWork().getYear()==LocalDate.now().getYear())
                this.leaveCurrentYear=true;
        }
        long day = ChronoUnit.DAYS.between((Temporal) startWork,(Temporal)LocalDate.now());
        if(day<=60)
            this.probation=true;
        this.availibleCurrentYear=furloughHistory.getAvailibleCurrentYear();
        if(user.getEndWork()!=null){
            int leftMonth = user.getEndWork().getMonthValue();
            int leftYear = user.getEndWork().getYear();
            int leftDate = user.getEndWork().getDayOfMonth();
            if(leftYear==LocalDate.now().getYear()){
                if(leftDate>=15)
                    this.availibleCurrentYear= 12-(12-leftMonth);
                else this.availibleCurrentYear=12-(12-leftMonth)+1;
            } else if(leftYear<LocalDate.now().getYear()) this.availibleCurrentYear=0;
        }
        this.oddCurrentYear = furloughHistory.getLeftFurlough();
        this.usedBeforeApril = furloughs.get(0).getUsedInMonth() + furloughs.get(1).getUsedInMonth() + furloughs.get(2).getUsedInMonth();
        for(int i=4;i<=11;i++){
            this.usedInYear=this.usedInYear+furloughs.get(i).getUsedInMonth();
        }
        this.leftLastYear = this.oddCurrentYear-this.usedBeforeApril;
        this.leftCurentYear=this.availibleCurrentYear-this.usedInYear+this.usedBeforeApril;
        if(this.usedBeforeApril>this.oddCurrentYear)
            this.leftCurentYear=this.leftCurentYear -(this.usedBeforeApril-this.oddCurrentYear);
        if(user.getAvalible())
            this.payFurlough=this.leftLastYear;
        else
            this.payFurlough=this.leftCurentYear + this.leftLastYear;
        if(furloughs.get(LocalDate.now().getMonthValue()-1).getAvailableUsedTillMonth()==null)
            this.availibleUsePresentMonth=this.availibleCurrentYear;
        else
        this.availibleUsePresentMonth=furloughs.get(LocalDate.now().getMonthValue()-1).getAvailableUsedTillMonth();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Furlough> getFurloughs() {
        return furloughs;
    }

    public void setFurloughs(List<Furlough> furloughs) {
        this.furloughs = furloughs;
    }

    public float getAvailibleCurrentYear() {
        return availibleCurrentYear;
    }

    public void setAvailibleCurrentYear(float availibleCurrentYear) {
        this.availibleCurrentYear = availibleCurrentYear;
    }

    public float getOddCurrentYear() {
        return oddCurrentYear;
    }

    public void setOddCurrentYear(float oddCurrentYear) {
        this.oddCurrentYear = oddCurrentYear;
    }

    public float getUsedBeforeApril() {
        return usedBeforeApril;
    }

    public void setUsedBeforeApril(float usedBeforeApril) {
        this.usedBeforeApril = usedBeforeApril;
    }

    public float getUsedInYear() {
        return usedInYear;
    }

    public void setUsedInYear(float usedInYear) {
        this.usedInYear = usedInYear;
    }

    public float getLeftLastYear() {
        return leftLastYear;
    }

    public void setLeftLastYear(float leftLastYear) {
        this.leftLastYear = leftLastYear;
    }

    public float getLeftCurentYear() {
        return leftCurentYear;
    }

    public void setLeftCurentYear(float leftCurentYear) {
        this.leftCurentYear = leftCurentYear;
    }

    public float getPayFurlough() {
        return payFurlough;
    }

    public void setPayFurlough(float payFurlough) {
        this.payFurlough = payFurlough;
    }

    public boolean isProbation() {
        return probation;
    }

    public void setProbation(boolean probation) {
        this.probation = probation;
    }

    public float getAvailibleUsePresentMonth() {
        return availibleUsePresentMonth;
    }

    public void setAvailibleUsePresentMonth(float availibleUsePresentMonth) {
        this.availibleUsePresentMonth = availibleUsePresentMonth;
    }
}
