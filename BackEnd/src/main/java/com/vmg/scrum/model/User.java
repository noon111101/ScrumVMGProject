package com.vmg.scrum.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.Department;
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
    @Column(length = 200, unique = true, nullable = false)
    private String username;

    @Column(length = 130,nullable = true)
    @JsonIgnore
    private String password;

    @Column(length = 130,nullable = false)
    @JsonIgnore
    private String rootPassword;
    @Column(length = 50, nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private double code;

    @Column(nullable = false)
    private String gender;
    @Column(length = 500,nullable = false)
    private String cover;

    @Column(columnDefinition = "boolean default false")
    private Boolean checkRootDisable ;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department departments;

    @Column(columnDefinition = "boolean default true")
    private Boolean avalible;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<LogDetail> logDetails= new HashSet<>();
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    public User(String username, String rootPassword, String fullName,String gender,String cover,Double code,Department department) {
        this.username = username;
        this.rootPassword = rootPassword;
        this.fullName = fullName;
        this.gender=gender;
        this.cover=cover;
        this.code=code;
        this.departments=department;
        this.checkRootDisable=false;
        this.avalible=true;
    }
    public User(String username, String fullName,String gender,Double code,Department department,String cover) {
        this.username = username;
        this.fullName = fullName;
        this.code=code;
        this.gender=gender;
        this.departments=department;
        this.checkRootDisable=false;
        this.avalible=true;
        this.cover=cover;
    }
}
