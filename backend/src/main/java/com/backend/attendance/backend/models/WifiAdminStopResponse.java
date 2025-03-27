package com.backend.attendance.backend.models;

public class WifiAdminStopResponse {
    String status;
    String message;

    public WifiAdminStopResponse() {}

    public WifiAdminStopResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
