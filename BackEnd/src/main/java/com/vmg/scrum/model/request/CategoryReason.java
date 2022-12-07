package com.vmg.scrum.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CategoryReason {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_reason_id", nullable = false)
    private Long id;

    @Column(name = "category_reason_name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_request_id", referencedColumnName = "category_request_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CatergoryRequest catergoryRequest;
}
