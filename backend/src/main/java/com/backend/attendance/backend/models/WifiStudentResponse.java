package com.backend.attendance.backend.models;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}

