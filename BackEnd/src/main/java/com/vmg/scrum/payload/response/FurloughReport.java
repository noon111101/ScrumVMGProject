package com.vmg.scrum.payload.response;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
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


    public FurloughReport(User user, List<Furlough> furloughs,Long year,FurloughHistory furloughHistory) {
        //Ngày giờ nghỉ làm
        Date leftTime = user.getModifiedDate();
        Long leftYear = (long)leftTime.getYear();
        int leftDate = leftTime.getDate();
        int leftMonth = leftTime.getMonth();
        LocalDate startWork = user.getStartWork();
        this.user = user;
        this.furloughs = furloughs;
        if(!user.getAvalible()){
            if(leftYear==year){
                if(leftDate>=15)
                    this.availibleCurrentYear= 12-leftMonth;
                else this.availibleCurrentYear=12-leftMonth+1;
            } else if(leftYear<year) this.availibleCurrentYear=0;
            else if(leftYear>year) this.availibleCurrentYear=furloughHistory.getAvailibleCurrentYear();
        } else {
            if(furloughHistory.getAvailibleCurrentYear()!=0)
                this.availibleCurrentYear = furloughHistory.getAvailibleCurrentYear();
            else {
                if (startWork.getYear() == year ) {
                    if (startWork.getDayOfMonth() >= 15)
                        this.availibleCurrentYear = 12 - startWork.getMonthValue();
                    else this.availibleCurrentYear = 12 - startWork.getMonthValue() + 1;
                }
                else this.availibleCurrentYear= 12;
            }
        }
        this.oddCurrentYear = furloughHistory.getLeftFurlough();
        this.usedBeforeApril = furloughs.get(0).getUsedInMonth() + furloughs.get(1).getUsedInMonth() + furloughs.get(2).getUsedInMonth();
        for(int i=0;i<=11;i++){
            this.usedInYear=this.usedInYear+furloughs.get(i).getUsedInMonth();
        }
        this.leftLastYear = this.oddCurrentYear-this.usedBeforeApril;
        if(this.leftLastYear<0)
            this.leftLastYear=0;
        this.leftCurentYear=this.availibleCurrentYear-this.usedInYear+this.usedBeforeApril;
        if(this.usedBeforeApril>this.oddCurrentYear)
            this.leftCurentYear=this.leftCurentYear -(this.usedBeforeApril-this.oddCurrentYear);
        if(user.getAvalible())
            this.payFurlough=this.leftLastYear;
        else
            this.payFurlough=this.leftCurentYear + this.leftLastYear;
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

    public float getAvailibleUsePresentMonth() {
        return availibleUsePresentMonth;
    }

    public void setAvailibleUsePresentMonth(float availibleUsePresentMonth) {
        this.availibleUsePresentMonth = availibleUsePresentMonth;
    }
}
