package com.vmg.scrum.model;

import com.vmg.scrum.model.option.Department;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "holiday_id", nullable = false)
    private Long id;

    @Column(name = "holiday_name", unique = true, nullable = false)
    private String holidayName;

    @Column(name = "date_from")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @Column(name = "is_Loop")
    private Boolean isLoop;

    public Holiday(String holidayName, LocalDate dateFrom, LocalDate dateTo, boolean isLoop) {
        this.holidayName = holidayName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.isLoop = isLoop;
    }
}
