package com.vmg.scrum.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.Cover;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.model.option.Status;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "code"),

})
public class User extends BaseEntity {
    @Column(length = 20, unique = true, nullable = false)
    private String username;
    @Column(length = 130,nullable = false)
    @JsonIgnore
    private String password;
    @Column(length = 50, nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private Double code;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnore
    private Department departments;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @JsonIgnore
    private Status statuses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<LogDetail> logDetails= new HashSet<>();
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
}
