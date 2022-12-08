package com.vmg.scrum.payload.request;

import lombok.Data;

@Data
public class ManageRequests_Request {

    private long user_id;

    private long depart_id;

    private String search;

    private long status;
}
