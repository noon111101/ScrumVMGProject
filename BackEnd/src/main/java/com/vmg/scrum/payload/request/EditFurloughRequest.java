package com.vmg.scrum.payload.request;

import com.vmg.scrum.pojo.FurloughCurrentEdit;
import com.vmg.scrum.pojo.FurloughPreviousEdit;
import com.vmg.scrum.pojo.MonthFurloughEdit;

import java.util.List;

public class EditFurloughRequest {
    private List<MonthFurloughEdit> monthFurloughEdits;

    private List<FurloughPreviousEdit> furloughPreviousEdits;

    private List<FurloughCurrentEdit> furloughCurrentEdits;

    private Long year;

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public List<MonthFurloughEdit> getMonthFurloughEdits() {
        return monthFurloughEdits;
    }

    public void setMonthFurloughEdits(List<MonthFurloughEdit> monthFurloughEdits) {
        this.monthFurloughEdits = monthFurloughEdits;
    }

    public List<FurloughPreviousEdit> getFurloughPreviousEdits() {
        return furloughPreviousEdits;
    }

    public void setFurloughPreviousEdits(List<FurloughPreviousEdit> furloughPreviousEdits) {
        this.furloughPreviousEdits = furloughPreviousEdits;
    }

    public List<FurloughCurrentEdit> getFurloughCurrentEdits() {
        return furloughCurrentEdits;
    }

    public void setFurloughCurrentEdits(List<FurloughCurrentEdit> furloughCurrentEdits) {
        this.furloughCurrentEdits = furloughCurrentEdits;
    }
}
