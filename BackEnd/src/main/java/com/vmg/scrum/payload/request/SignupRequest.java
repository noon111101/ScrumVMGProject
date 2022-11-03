package com.vmg.scrum.payload.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank(message = "Chưa nhập email")
    @Size(min = 3, max = 50)
    private String username;

    private Set<String> role;

    @NotBlank(message = "Chưa nhập tên")
    @Size(min = 6, max = 50)
    private String fullName;

//    @NotBlank(message = "Chưa nhập mã nhân viên")
    private Double code;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @NotBlank(message = "Chưa chọn phòng ban")
    private String department;
    @NotBlank(message = "Chưa chọn giới tính")
    private String gender;

    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }

    @NotNull(message = "Chưa upload ảnh")
    private MultipartFile cover;




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
