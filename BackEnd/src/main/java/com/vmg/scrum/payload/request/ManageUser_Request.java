package com.vmg.scrum.payload.request;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class ManageUser_Request {
    private Long departid;
    private String search;
    private Boolean status;
}
