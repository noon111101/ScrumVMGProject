package com.vmg.scrum.model.option;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CatergoryRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "catergory_request_id", nullable = false)
    private Long id;

    private String catergory;

    private String name;



}
