package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiAdminStartRequest {

    private String subject;
    private String batch;
    private String year;

    private WifiAdminStartRequest() {
    }

    public WifiAdminStartRequest(String subject, String batch, String year) {
        this.subject = subject;
        this.batch = batch;
        this.year = year;
    }

}
