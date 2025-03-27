package com.backend.attendance.backend.models;

import lombok.Getter;

@Getter
public class WifiAdminStartResponse {
    private String success;
    public WifiAdminStartResponse(String success) {
        this.success = success;
    }
}
