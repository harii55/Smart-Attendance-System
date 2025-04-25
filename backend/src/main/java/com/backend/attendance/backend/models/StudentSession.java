package com.backend.attendance.backend.models;

import org.springframework.web.socket.WebSocketSession;

public class StudentSession {
    String email;
    String bssid;
    Long connectionStartTime;
    Long totalConnectionTime;
    WebSocketSession session;
    Long lastPingTime;
    String batch;
    String year;
    String subject;
    Boolean isConnected;

    public StudentSession(String email, String bssid, Long startTime, Long totalConnectionTime, WebSocketSession session, String batch, String year, String subject, Boolean isConnected) {
        this.email = email;
        this.bssid = bssid;
        this.connectionStartTime = startTime;
        this.totalConnectionTime = totalConnectionTime;
        this.session = session;
        this.batch = batch;
        this.year = year;
        this.subject = subject;
        this.isConnected = isConnected;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Long getConnectionStartTime() {
        return connectionStartTime;
    }

    public void setConnectionStartTime(Long connectionStartTime) {
        this.connectionStartTime = connectionStartTime;
    }

    public Long getTotalConnectionTime() {
        return totalConnectionTime;
    }

    public void setTotalConnectionTime(Long totalConnectionTime) {
        this.totalConnectionTime = totalConnectionTime;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getLastPingTime() {
        return lastPingTime;
    }

    public void setLastPingTime(Long lastPingTime) {
        this.lastPingTime = lastPingTime;
    }

    public Boolean getIsConnected() {
        return isConnected;
    }
    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }
}
