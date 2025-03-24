package com.backend.attendance.backend.models;

public class WifiAdminStopRequest {

    private String subject;
    private String batch;
    private String year;
    private Boolean monitoring;

    private WifiAdminStopRequest() {
    }

    public WifiAdminStopRequest(String subject, String batch, Boolean monitoring) {
        this.subject = subject;
        this.batch = batch;
        this.year = year;
        this.monitoring = monitoring;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public Boolean getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(Boolean monitoring) {
        this.monitoring = monitoring;
    }
}
