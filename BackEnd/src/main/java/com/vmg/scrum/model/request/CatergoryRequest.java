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
    @Column(name = "catergory_request_id", nullable = false)
    private Long id;

    @Column(name = "catergory_request_name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "catergoryRequest")
    @JsonIgnore
    private Set<CategoryReason> categoryReasons;
}
