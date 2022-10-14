package com.vmg.scrum.model.excel;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.ExceptionLog;
import com.vmg.scrum.model.option.Shift;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class LogDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private Date date;

    private String in;

    private String out;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exceptionLog_id", referencedColumnName = "id")
    private ExceptionLog exception;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;

    private String leave;

}
