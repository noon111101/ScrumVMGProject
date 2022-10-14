package com.vmg.scrum.model;


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
})
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String username;
    @Column(length = 130,nullable = false)
    private String password;
    @Column(length = 50, nullable = false)
    private String fullName;
    @Column(nullable = false)


    private Double code;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department departments;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status statuses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
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
