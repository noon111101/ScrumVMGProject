package com.vmg.scrum.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Chưa nhập tên")
    private String username;

    @NotBlank(message = "Chưa nhập mật khẩu")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}