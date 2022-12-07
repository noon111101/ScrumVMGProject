package com.vmg.scrum.service;

import com.vmg.scrum.payload.request.EditFurloughRequest;
import com.vmg.scrum.payload.response.FurloughReport;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface FurloughService {

   Map<String, List<FurloughReport>> getAllFurloughByYear(Long year,String departName);

   MessageResponse editFurloughReport(EditFurloughRequest editFurloughRequest);

   public List<FurloughReport> getFurloughsByYear(Long year);

}
