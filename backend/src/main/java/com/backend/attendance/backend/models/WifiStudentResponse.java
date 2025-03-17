package com.backend.attendance.backend.models;

public class WifiStudentResponse {


    private String ipAddress;
    private String status;

    public WifiStudentResponse(String ipAddress, String status) {
        this.ipAddress = ipAddress;
        this.status = status;
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


}

