package com.vmg.scrum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "holiday_id", nullable = false)
    private Long id;

    @Column(name = "holiday_name", unique = true, nullable = false)
    private String holidayName;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;


    @Column(name = "is_Loop")
    private Boolean isLoop;


}
