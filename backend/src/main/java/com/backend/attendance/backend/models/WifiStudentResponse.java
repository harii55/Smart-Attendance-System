package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiStudentResponse {

    private String message;
    private String ipAddress;
    private String status;

    public WifiStudentResponse(String ipAddress, String status, String message) {
        this.ipAddress = ipAddress;
        this.status = status;
        this.message = message;
    }

    private WifiStudentResponse() {
    }

}

