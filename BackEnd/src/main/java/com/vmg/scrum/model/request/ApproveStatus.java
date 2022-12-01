package com.vmg.scrum.model.request;



import lombok.*;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ApproveStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "approve_status_id", nullable = false)
    private Long id;


    @Column(name = "approve_status_Name", nullable = false)
    private String name;
}
