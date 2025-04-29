package com.android.attendance_automation_android;

public class LoginRequest {
    private String email;
    private String password;
    private String ip;

    public LoginRequest(String email, String password, String ip) {
        this.email = email;
        this.password = password;
        this.ip = ip;
    }
}