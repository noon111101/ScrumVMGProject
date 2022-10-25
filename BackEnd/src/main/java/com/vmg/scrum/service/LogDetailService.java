package com.vmg.scrum.service;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LogDetailService {
    MessageResponse updateLogDetails(EditLogRequest[] editLogRequest);
}
