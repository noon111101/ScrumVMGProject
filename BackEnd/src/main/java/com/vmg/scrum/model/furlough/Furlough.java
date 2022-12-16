package com.vmg.scrum.model.furlough;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.User;
import com.vmg.scrum.repository.FurloughHistoryRepository;
import com.vmg.scrum.repository.FurloughRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    //Tháng trong năm
    private Long monthInYear;

    //năm
    private Long year;

    //Số phép dùng trong tháng
    @Column(columnDefinition = "float default 0")
    private Float usedInMonth;

    private Float availableUsedTillMonth;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Furlough(Long monthInYear, Long year, Float usedInMonth, User user ,Float availableUsedTillMonth) {
        this.monthInYear = monthInYear;
        this.year = year;
        this.usedInMonth = usedInMonth;
        this.user = user;
        this.availableUsedTillMonth=availableUsedTillMonth;
    }


}
