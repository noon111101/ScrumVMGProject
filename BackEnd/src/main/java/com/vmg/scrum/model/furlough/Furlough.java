package com.vmg.scrum.model.furlough;

import com.vmg.scrum.model.User;
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
public class Furlough {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "furlough_id", nullable = false)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM")
    private LocalDate monthInYear;

    private float usedInMonth;

    private float payFurlough;

    private float currentYearFurlough;

    private float previousYearFurlough;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
