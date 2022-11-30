package com.vmg.scrum.model;


import com.vmg.scrum.model.option.Department;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


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

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Column(name = "date_from")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @Column(name = "total_days")
    private int totalDays;


    public Holiday(String holidayName, LocalDate dateFrom, LocalDate dateTo) {
        this.holidayName = holidayName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        totalDays = dateTo.compareTo(dateFrom)+1;
    }

}
