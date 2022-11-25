package com.vmg.scrum.service;

import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.request.ImageLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LogDetailService {
    MessageResponse updateLogDetails(EditLogRequest[] editLogRequest);

    String sendImg(ImageLogRequest imageLogRequest);
}
