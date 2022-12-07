package com.vmg.scrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {


    @Column
    @CreatedDate
    @JsonIgnore
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date createDate;
    @Column
    @LastModifiedDate
    @JsonIgnore
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date modifiedDate;
    @Column
    @CreatedBy
    @JsonIgnore
    private String createBy;
    @Column
    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;



}
