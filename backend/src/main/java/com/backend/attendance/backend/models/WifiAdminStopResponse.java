package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiAdminStopResponse {
    String status;
    String message;

    public WifiAdminStopResponse() {}

    public WifiAdminStopResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }


}
