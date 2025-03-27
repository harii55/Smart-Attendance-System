package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiAdminStopRequest {

    private String subject;
    private String batch;
    private String year;
    private Boolean monitoring;

    private WifiAdminStopRequest() {
    }

    public WifiAdminStopRequest(String subject, String batch, Boolean monitoring) {
        this.subject = subject;
        this.batch = batch;
        this.year = year;
        this.monitoring = monitoring;
    }


}
