package com.backend.attendance.backend.models;

public class WifiAdminStartResponse {
    private Boolean success;

    public WifiAdminStartResponse() {}

    public WifiAdminStartResponse(Boolean success) {
        this.success = success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
