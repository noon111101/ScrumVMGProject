package com.vmg.scrum.payload.response;

import com.vmg.scrum.model.excel.LogDetail;

import java.util.List;

public class UserLogDetail {
    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public UserLogDetail() {
    }

    public List<LogDetail> getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(List<LogDetail> logDetail) {
        this.logDetail = logDetail;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<LogDetail> logDetail;

    private Double code;
    private String name;
}
