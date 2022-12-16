package com.vmg.scrum.model.furlough;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.User;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FurloughHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furlough_history_id", nullable = false)
    private Long furlough_history_id;

    private float leftFurlough;

    private float availibleCurrentYear;

    private Long year;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

}
