package com.vmg.scrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.request.Request;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Sign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sign_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_name",length = 20)
    private ESign name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "signs")
    @JsonIgnore
    private Set<LogDetail> logDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "lastSign")
    @JsonIgnore
    private Set<Request> requests = new HashSet<>();


    public Sign(ESign nt) {
        this.name=nt;
    }
}
