package com.vmg.scrum.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ForgotPasswordChangeRequest {
    @NotNull(message = "Link không chứa token xác định người dùng")
    private String token;

    @NotBlank(message = "Chưa nhập mật khẩu mới")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
