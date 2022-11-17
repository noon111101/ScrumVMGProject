package com.vmg.scrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",length = 20)
    private ERole name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;
}
