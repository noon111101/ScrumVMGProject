package com.vmg.scrum.model.excel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Shift;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LogDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private LocalDate dateLog;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private LocalTime regularHour;

    private LocalTime overTime;

    private LocalTime totalWork;

    private String exception;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign signs ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id")
    @JsonIgnore
    private Shift shift;

    private String leaveStatus;

    private String reason;

}
