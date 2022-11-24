package com.vmg.scrum.model.off;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OffHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "off_history_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    private float usedInMonth;

    private LocalDate monthInYear ;


}
