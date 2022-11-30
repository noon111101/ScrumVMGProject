package com.vmg.scrum.service;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.EditLogRequest;
import com.vmg.scrum.payload.request.FaceKeepRequest;
import com.vmg.scrum.payload.request.ImageLogRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LogDetailService {
    MessageResponse updateLogDetails(EditLogRequest[] editLogRequest);

    String sendImg(ImageLogRequest imageLogRequest);

    LogDetail faceTimeKeep(FaceKeepRequest faceKeepRequest);
}
