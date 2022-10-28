package com.vmg.scrum.model.excel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Shift;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LogDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private LocalDate date_log;

    private LocalTime timeIn;

    private LocalTime timeOut;

    private LocalTime regularHour;

    private LocalTime overTime;

    private LocalTime totalWork;

    private String exception;

    @ManyToOne
    @JoinColumn(name = "signs_id")
    private Sign signs ;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    @JsonIgnore
    private Shift shift;

    private String leave_status;



}
