package com.vmg.scrum.model.request;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SessionOff {
//    Loại nghỉ buổi nào
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_off_id", nullable = false)
    private Long id;

    @Column(name = "session_off_name")
    private String name;
}
