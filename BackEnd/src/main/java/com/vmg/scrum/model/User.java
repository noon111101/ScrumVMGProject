package com.vmg.scrum.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.furlough.Furlough;
import com.vmg.scrum.model.furlough.FurloughHistory;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.model.option.NoteLog;
import com.vmg.scrum.model.request.CategoryReason;
import com.vmg.scrum.model.request.Request;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    private String code;

    @Column(nullable = false)
    private String gender;
    @Column(length = 500)
    private String cover;

    @Column(columnDefinition = "boolean default false")
    private Boolean checkRootDisable ;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department departments;

    @ManyToMany(mappedBy = "approvers")
    @JsonIgnore
    private Set<Request> requestApprovers;

    @ManyToMany(mappedBy = "followers")
    @JsonIgnore
    private Set<Request> requestFollowers;

    @Column(columnDefinition = "boolean default true")
    private Boolean avalible;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<LogDetail> logDetails= new HashSet<>();
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position position;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private Set<Furlough> furloughs;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore

    private Set<FurloughHistory> pastYearFurlough;

    @Column(name = "date_startwork", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startWork;

    @Column(name = "date_endwork", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endWork;


    public User(String username, String rootPassword, String fullName,String gender,String cover,String code,Department department,Position position,LocalDate startWork, LocalDate endWork) {
        this.username = username;
        this.rootPassword = rootPassword;
        this.fullName = fullName;
        this.gender=gender;
        this.cover=cover;
        this.code=code;
        this.departments=department;
        this.checkRootDisable=false;
        this.avalible =true;
        this.position = position;
        this.startWork = startWork;
        this.endWork = endWork;
    }

    public User(String username, String fullName,String gender,String code,Department department,String cover, LocalDate startWork, LocalDate endWork ) {
        this.username = username;
        this.fullName = fullName;
        this.code=code;
        this.gender=gender;
        this.departments=department;
        this.checkRootDisable=false;
        this.avalible =true;
        this.cover=cover;
        this.startWork = startWork;
        this.endWork = endWork;
    }

}
