package com.backend.attendance.backend.models;

public class WifiAdminStartResponse {
    private String success;
    public WifiAdminStartResponse(String success) {
        this.success = success;
    }
    public String getSuccess() {
        return success;
    }
}
