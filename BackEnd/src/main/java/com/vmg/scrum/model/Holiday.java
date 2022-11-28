package com.vmg.scrum.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "holiday_id", nullable = false)
    private Long id;

    @Column(name = "holiday_name", unique = true, nullable = false)
    private String holidayname;

    @Column(name = "date_from")
    private LocalDate datefrom;

    @Column(name = "date_to")
    private LocalDate dateto;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_Many")
    private Boolean isMany;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolidayname() {
        return holidayname;
    }

    public void setHolidayname(String holidayname) {
        this.holidayname = holidayname;
    }

    public LocalDate getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(LocalDate datefrom) {
        this.datefrom = datefrom;
    }

    public LocalDate getDateto() {
        return dateto;
    }

    public void setDateto(LocalDate dateto) {
        this.dateto = dateto;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getMany() {
        return isMany;
    }

    public void setMany(Boolean many) {
        isMany = many;
    }
}
