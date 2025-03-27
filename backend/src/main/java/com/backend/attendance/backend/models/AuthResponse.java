package com.backend.attendance.backend.models;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    public AuthResponse(String token) {
        this.token = token;
    }
}
