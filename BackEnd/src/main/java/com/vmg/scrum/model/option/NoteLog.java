package com.vmg.scrum.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.NoteCatergory;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class NoteLog  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_log_id", nullable = false)
    private Long note_log_id;

    @Column
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "admin_edit_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User adminEdit;

    @ManyToOne
    @JoinColumn(name = "log_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private LogDetail logDetail;

    @ManyToOne
    @JoinColumn(name = "approvers_request_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User approversRequest;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_catergory_id", referencedColumnName = "note_catergory_id")
    private NoteCatergory noteCatergory;

    @OneToOne
    private Sign lastSign;

    @OneToOne
    private Sign signChange;

}
