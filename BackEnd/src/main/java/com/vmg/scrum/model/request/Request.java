package com.vmg.scrum.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User creator;

    @OneToMany(mappedBy = "request")
    private Set<User> approvers;

    @OneToMany(mappedBy = "request")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    @JsonIgnore
    private Session session;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign lastSign;

}
