package com.backend.attendance.backend.models;

public class WifiAdminStopResponse {
    String status;
    String message;

    public WifiAdminStopResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
