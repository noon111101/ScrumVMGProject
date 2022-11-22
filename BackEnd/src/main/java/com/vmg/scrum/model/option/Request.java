package com.vmg.scrum.model.option;

import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Request extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id", nullable = false)
    private Long id;

    private String title;

    @OneToOne
    private User by;

    @OneToMany(mappedBy = "request")
    private Set<User> approve;

    @OneToMany(mappedBy = "request")
    private Set<User> follow;

    private String content;

}
