package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiStudentResponse {

    private String message;
    private String status;
    private int statusCodeValue;

    public WifiStudentResponse(String status, String message, int statusCodeValue) {
        this.status = status;
        this.message = message;
        this.statusCodeValue = statusCodeValue;
    }

    private WifiStudentResponse() {
    }

}

