package com.vmg.scrum.service;

import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;

public interface LogDetailService {
    MessageResponse updateLogDetails(EditLogRequest[] editLogRequest);
}
