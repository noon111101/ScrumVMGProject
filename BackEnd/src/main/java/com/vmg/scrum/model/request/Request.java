package com.vmg.scrum.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import lombok.*;

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

    @ManyToMany(mappedBy = "requestApprovers")
    private Set<User> approvers;

    @ManyToMany(mappedBy = "requestFollowers")
    private Set<User> followers;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approve_status_id", referencedColumnName = "approve_status_id")
    private ApproveStatus approveStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catergory_request_id", referencedColumnName = "catergory_request_id")
    @JsonIgnore
    private CatergoryRequest catergoryRequest;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign lastSign;

    public Request(String title, User creator,String content, CatergoryRequest catergoryRequest ,LocalDate dateFrom, LocalDate dateTo,Session session,Sign lastSign) {
             this.title = title;
             this.creator = creator;
             this.content = content;
             this.catergoryRequest = catergoryRequest;
             this.dateFrom = dateFrom;
             this.dateTo = dateTo;
             this.session = session;
             this.lastSign= lastSign;
    }




}
