package com.vmg.scrum.model.request;


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
public class CatergoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_request_id", nullable = false)
    private Long id;

    @Column(name = "category_request_name")
    private String name;



}
