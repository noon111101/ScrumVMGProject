package com.vmg.scrum.model.excel;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LogDetailTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_detail_id", nullable = false)
    private Long id;

    private String name;

    private LocalDateTime regularHour;

    private LocalDateTime overTime;

    private LocalDateTime totalWork;

}