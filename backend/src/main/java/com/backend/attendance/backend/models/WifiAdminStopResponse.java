package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiAdminStopResponse {
    private String success;
    private String status;
    private int statusCodeValue;
    private String message;

    public WifiAdminStopResponse() {}

    public WifiAdminStopResponse(String success, String status, String message, int statusCodeValue) {
        this.success = success;
        this.status = status;
        this.statusCodeValue = statusCodeValue;
        this.message = message;
    }


}
