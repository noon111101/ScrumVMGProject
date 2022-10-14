package com.vmg.scrum.model.option;

import com.vmg.scrum.model.excel.LogDetail;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "shift")
    private LogDetail logDetail;

}
