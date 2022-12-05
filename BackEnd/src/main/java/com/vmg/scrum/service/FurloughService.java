package com.vmg.scrum.service;

import com.vmg.scrum.payload.response.FurloughReport;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface FurloughService {
   public Map<String, List<FurloughReport>> getAllFurloughByYear(Long year);

}
