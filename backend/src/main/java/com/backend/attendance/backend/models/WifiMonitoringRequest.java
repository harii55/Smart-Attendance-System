package com.backend.attendance.backend.models;

public class WifiMonitoringRequest {
    private String email;
    private String ipAddress;
    private String batchName;

    public WifiMonitoringRequest(String email, String ipAddress, String batchName) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.batchName = batchName;
    }

    private WifiMonitoringRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getBatchName() {
        return batchName;
    }

}
