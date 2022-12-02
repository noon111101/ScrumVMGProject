package com.vmg.scrum.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmg.scrum.model.Sign;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.ApproveStatus;
import com.vmg.scrum.model.request.CatergoryRequest;
import com.vmg.scrum.model.request.Session;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class OfferRequest {
    @Id
    private Long id;

    @NotBlank(message = "Chưa nhập tên ngày nghỉ")
    @Size(min = 3, max = 50)
    private String title;

    private User creator;

    private Set<String> approvers;

    private Set<String> followers;

    @NotBlank(message = "Chưa nhập tên ngày nghỉ")
    @Size(min = 3, max = 50)
    private String content;

    private ApproveStatus approveStatus;

    private CatergoryRequest catergoryRequest;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    private Session session;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
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

    public ApproveStatus getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(ApproveStatus approveStatus) {
        this.approveStatus = approveStatus;
    }

    public CatergoryRequest getCatergoryRequest() {
        return catergoryRequest;
    }

    public void setCatergoryRequest(CatergoryRequest catergoryRequest) {
        this.catergoryRequest = catergoryRequest;
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Sign getLastSign() {
        return lastSign;
    }

    public void setLastSign(Sign lastSign) {
        this.lastSign = lastSign;
    }
}
