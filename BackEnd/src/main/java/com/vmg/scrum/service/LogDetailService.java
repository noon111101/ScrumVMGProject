package com.vmg.scrum.service;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LogDetailService {
//    Page<LogDetail> findByUserCode(Pageable pageable, Double code);


//    Page<LogDetailDTO> getLogs(Pageable pageable, int page, int size);

    List<LogDetail>  getLogDetailsByDpD (String key, LocalDate date, Pageable pageable);
    List<LogDetail>  getLogDetailsByD (LocalDate date, Pageable pageable);

    List<LogDetail>  getLogDetailsByDp (String key, Pageable pageable);

    List<LogDetail> getLogAllDetails(Pageable pageable);
}
