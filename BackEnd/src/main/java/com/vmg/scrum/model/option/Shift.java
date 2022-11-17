package com.vmg.scrum.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.excel.LogDetail;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "shift_name")
    private String name;


}
