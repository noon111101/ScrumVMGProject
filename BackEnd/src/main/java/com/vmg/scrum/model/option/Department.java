package com.vmg.scrum.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.User;
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
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "departments")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

}
