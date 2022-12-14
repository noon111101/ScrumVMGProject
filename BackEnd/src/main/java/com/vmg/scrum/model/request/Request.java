package com.vmg.scrum.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Request extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id", nullable = false)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany()
    @JoinTable(
            name = "request_approvers",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> approvers;

    @ManyToMany()

    @JoinTable(
            name = "request_followers",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> followers;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approve_status_id", referencedColumnName = "approve_status_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApproveStatus approveStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_request_id", referencedColumnName = "category_request_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CatergoryRequest catergoryRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_reason_id", referencedColumnName = "category_reason_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CategoryReason categoryReason;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateForget;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeStart;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEnd;

    private Float countOff;
    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign lastSign;

    @Column(name = "total_days")
    private double totalDays;

    public Request(User creator, String title, String content, ApproveStatus approveStatus, CategoryReason categoryReason, CatergoryRequest catergoryRequest, LocalDate dateFrom, LocalDate dateTo, LocalDate dateForget, LocalTime timeStart, LocalTime timeEnd, Sign lastSign) {
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.approveStatus = approveStatus;
        this.categoryReason = categoryReason;
        this.catergoryRequest = catergoryRequest;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateForget = dateForget;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.lastSign = lastSign;

//        LocalTime s1 = LocalTime.of(7, 59);
//        LocalTime s2 = LocalTime.of(11, 59);
//        LocalTime s3 = LocalTime.of(9, 1);
//        LocalTime c1 = LocalTime.of(12, 59);
//        LocalTime c2 = LocalTime.of(17, 59);
//        LocalTime c3 = LocalTime.of(13, 01);
//
//        if (dateFrom.equals(dateTo)) {
//            if (timeStart.isAfter(s1) && timeEnd.isBefore(c1)) {
//                totalDays = dateTo.compareTo(dateFrom) + 0.5;
//            } else if (timeStart.isAfter(c1) && timeEnd.isBefore(c1)) {
//                totalDays = dateTo.compareTo(dateFrom) + 0.5;
//            } else {
//                totalDays = dateTo.compareTo(dateFrom) + 1;
//            }
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s1) && timeEnd.isBefore(c1)) {
//            totalDays = dateTo.compareTo(dateFrom);
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s1) && timeStart.isBefore(s2) && timeEnd.isBefore(c1)) {
//            totalDays = dateTo.compareTo(dateFrom) + 0.5;
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s2) && timeEnd.isBefore(c2)) {
//            totalDays = dateTo.compareTo(dateFrom);
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s2) && timeEnd.isAfter(c1) && timeEnd.isBefore(c2)) {
//            totalDays = dateTo.compareTo(dateFrom) + 0.5;
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s1) && timeStart.isBefore(s2) && timeEnd.isAfter(c3) && timeEnd.isBefore(c2)) {
//            totalDays = dateTo.compareTo(dateFrom) + 1;
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s1) && timeStart.isBefore(s2) && timeEnd.isAfter(c1) && timeEnd.isBefore(c3)) {
//            totalDays = dateTo.compareTo(dateFrom) + 0.5;
//        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(c3) && timeStart.isBefore(c2) && timeEnd.isAfter(s1) && timeEnd.isBefore(s3)) {
//            totalDays = dateTo.compareTo(dateFrom) + 0.5;
////        } else if (!dateFrom.equals(dateTo) && timeStart.isAfter(s1) && timeStart.isBefore(s3) && timeEnd.isAfter(c1) && timeEnd.isBefore(c3)) {
////            totalDays = dateTo.compareTo(dateFrom) + 1;
//        }

        LocalTime h8 = LocalTime.of(8, 0);
        LocalTime h10 = LocalTime.of(10, 0);
        LocalTime h12 = LocalTime.of(12, 0);
        LocalTime h13 = LocalTime.of(13, 0);
        LocalTime h15 = LocalTime.of(15, 0);

        if (dateFrom.equals(dateTo)) {
            if(timeStart.isBefore(h10) && !timeEnd.isBefore(h15)){
                totalDays = 1;
            }
            else if(timeStart.isBefore(h10) && timeEnd.isBefore(h13)){
                totalDays = 0.5;
            }
            else if(timeStart.isBefore(h10) && !timeEnd.isBefore(h13)){
                totalDays = 1;
            }
            else if(!timeStart.isBefore(h10) && !timeEnd.isBefore(h15)){
                totalDays = 0.5;
            }
            else {
                totalDays = 0;
            }
        } else if (dateFrom.isBefore(dateTo)) {
            if(timeStart.isBefore(h10) && !timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom) + 1;;
            }
            else if(timeStart.isBefore(h10) && !timeEnd.isBefore(h10) && timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom) + 0.5;
            }
            else if(!timeStart.isBefore(h10) && timeStart.isBefore(h13) && timeEnd.isBefore(h8)){
                totalDays = dateTo.compareTo(dateFrom) - 0.5;
            }
            else if(!timeStart.isBefore(h10) && timeStart.isBefore(h13) && !timeEnd.isBefore(h8) && timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom);
            }
            else if(!timeStart.isBefore(h10) && timeStart.isBefore(h13) && !timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom) + 0.5;
            }
            else if(!timeStart.isBefore(h13) && timeEnd.isBefore(h8)){
                totalDays = dateTo.compareTo(dateFrom) - 0.5;
            }
            else if(!timeStart.isBefore(h13) && !timeEnd.isBefore(h10) && timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom);
            }
            else if(!timeStart.isBefore(h13) && !timeEnd.isBefore(h13)){
                totalDays = dateTo.compareTo(dateFrom);
            }

            else{
                totalDays = 0;
            }
        }

    }

}
