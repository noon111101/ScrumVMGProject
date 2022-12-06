package com.vmg.scrum.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.model.request.CatergoryRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class OfferRequest {
    @Id
    private Long id;

    @NotBlank(message = "Chưa nhập tên ngày nghỉ")
    @Size(min = 3, max = 50)
    private String title;

    private String creator;

    private Set<String> approvers;

    private Set<String> followers;

    @NotBlank(message = "Chưa nhập tên ngày nghỉ")
    @Size(min = 3, max = 50)
    private String content;

    private long approveStatus;

    private long catergoryRequest;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeStart;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEnd;

    private Sign lastSign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Set<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<String> approvers) {
        this.approvers = approvers;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Sign getLastSign() {
        return lastSign;
    }

    public void setLastSign(Sign lastSign) {
        this.lastSign = lastSign;
    }

    public long getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(long approveStatus) {
        this.approveStatus = approveStatus;
    }

    public long getCatergoryRequest() {
        return catergoryRequest;
    }

    public void setCatergoryRequest(long catergoryRequest) {
        this.catergoryRequest = catergoryRequest;
    }
}
