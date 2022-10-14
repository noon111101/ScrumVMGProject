package com.vmg.scrum.model.option;

import com.vmg.scrum.model.excel.LogDetail;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ExceptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "exception")
    private LogDetail logDetail;
}
