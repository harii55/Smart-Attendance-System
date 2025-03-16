package com.backend.attendance.backend.models;

public class MonitoringStartRequest {

    private String subject;

    private MonitoringStartRequest() {
    }

    public MonitoringStartRequest(String subject) {
        this.subject = subject;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
