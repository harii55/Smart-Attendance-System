package com.backend.attendance.backend.models;

import lombok.Getter;

@Getter
public class WifiAdminStartResponse {
    private String success;
    private String statusCode;
    private int statusCodeValue;
    private String message;

    public WifiAdminStartResponse(String success, String statusCode, int statusCodeValue,  String message) {
        this.success = success;
        this.statusCode = statusCode;
        this.statusCodeValue = statusCodeValue;
        this.message = message;
    }
}
