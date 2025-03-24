package com.android.attendance_automation_android;

public class LoginResponse {
    private String token;
    private boolean new_user;

    public LoginResponse() {}
    public String getToken() {
        return token;
    }

    public boolean isNewUser() {
        return new_user;
    }
}
