package com.vmg.scrum.model.request;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_id", nullable = false)
    private Long id;

    @Column(name = "session_name")
    private String name;
}
