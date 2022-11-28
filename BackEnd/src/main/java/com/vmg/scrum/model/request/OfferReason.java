package com.vmg.scrum.model.request;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OfferReason {
//    Lý do nghỉ
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_reason_id", nullable = false)
    private Long id;

    @Column(name = "off_reason_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "catergory_request_id", nullable = false)
    private CatergoryRequest catergoryRequest;
}
