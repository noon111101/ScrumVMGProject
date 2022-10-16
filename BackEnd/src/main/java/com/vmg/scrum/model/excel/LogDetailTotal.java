package com.vmg.scrum.model.excel;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class LogDetailTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private LocalDateTime regularHour;

    private LocalDateTime overTime;

    private LocalDateTime totalWork;

}