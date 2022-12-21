package com.vmg.scrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.option.NoteLog;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class NoteCatergory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_catergory_id", nullable = false)
    private Long note_catergory_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_catergory_name",length = 20)
    private ENoteCatergory name;

}
