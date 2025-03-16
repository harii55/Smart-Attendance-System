package com.backend.attendance.backend.models;

public class WifiMonitoringResponse {


    private String ipAddress;
    private String status;

    public WifiMonitoringResponse(String ipAddress, String status) {
        this.ipAddress = ipAddress;
        this.status = status;
    }

    private WifiMonitoringResponse() {
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

