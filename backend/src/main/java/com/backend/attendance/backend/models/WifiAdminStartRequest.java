package com.backend.attendance.backend.models;

public class WifiAdminStartRequest {

    private String subject;

    private WifiAdminStartRequest() {
    }

    public WifiAdminStartRequest(String subject) {
        this.subject = subject;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
