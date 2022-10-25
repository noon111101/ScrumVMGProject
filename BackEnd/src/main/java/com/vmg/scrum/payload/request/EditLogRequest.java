package com.vmg.scrum.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EditLogRequest {
    @NotNull
    private double code;

    @NotNull
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NotBlank
    private String date;
}
