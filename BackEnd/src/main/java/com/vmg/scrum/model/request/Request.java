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
    @JoinColumn(name = "by_id", referencedColumnName = "user_id")
    private User by;

    @OneToMany(mappedBy = "request")
    private Set<User> approve;

    @OneToMany(mappedBy = "request")
    private Set<User> follow;

    private String content;

    @Column(columnDefinition = "boolean default true")
    private boolean approveStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catergory_request_id", referencedColumnName = "catergory_request_id")
    @JsonIgnore
    private CatergoryRequest catergoryRequest;

    private LocalDate fromDate;

    private LocalDate toDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_off_id", referencedColumnName = "session_off_id")
    @JsonIgnore
    private SessionOff sessionOff;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign lastSign;

}
