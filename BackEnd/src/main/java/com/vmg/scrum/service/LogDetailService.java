package com.vmg.scrum.service;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogDetailService {
    Page<LogDetail> findByUserCode(Pageable pageable, Double code);

}
