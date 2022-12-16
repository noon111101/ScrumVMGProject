package com.vmg.scrum.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EditLogRequest {
    @NotNull
    private String code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @NotNull
    private String sign;

    public String getCodeAdminEdit() {
        return codeAdminEdit;
    }

    public void setCodeAdminEdit(String codeAdminEdit) {
        this.codeAdminEdit = codeAdminEdit;
    }

    @NotBlank
    private String date;

    private String reason;

    private String codeAdminEdit;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
