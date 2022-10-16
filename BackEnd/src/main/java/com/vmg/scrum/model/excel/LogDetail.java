package com.vmg.scrum.model.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Shift;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private Date date_log;

    private LocalDateTime timeIn;

    private LocalDateTime timeOut;

    private LocalDateTime regularHour;

    private LocalDateTime overTime;

    private LocalDateTime totalWork;

    private String exception;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;

    private String leave_status;

}
