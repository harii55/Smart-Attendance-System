package com.backend.attendance.backend.models;

public class WifiStudentRequest {
    private String email;
    private String ipAddress;
    private String batchName;
    private String year;


    public WifiStudentRequest(String email, String ipAddress, String batchName) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.batchName = batchName;
        this.year = year;
    }

    private WifiStudentRequest() {
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

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

}
