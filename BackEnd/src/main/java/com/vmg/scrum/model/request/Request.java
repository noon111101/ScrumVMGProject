package com.vmg.scrum.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
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
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeStart;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEnd;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign lastSign;

    public Request(User creator, String title, String content, ApproveStatus approveStatus, CatergoryRequest catergoryRequest , LocalDate dateFrom, LocalDate dateTo, LocalTime timeStart, LocalTime timeEnd, Sign lastSign) {
             this.creator = creator;
             this.title = title;
             this.content = content;
             this.approveStatus = approveStatus;
             this.catergoryRequest = catergoryRequest;
             this.dateFrom = dateFrom;
             this.dateTo = dateTo;
             this.timeStart = timeStart;
             this.timeEnd = timeEnd;
             this.lastSign= lastSign;
    }




}
