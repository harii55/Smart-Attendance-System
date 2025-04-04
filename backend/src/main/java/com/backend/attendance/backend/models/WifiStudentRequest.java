package com.backend.attendance.backend.models;

import lombok.Getter;
import lombok.Setter;

public class WifiStudentRequest {
    @Setter
    @Getter
    private String email;
    @Getter
    @Setter
    private String bssid;
    @Getter
    @Setter
    private String batchName;
    @Getter
    @Setter
    private String year;


    public WifiStudentRequest(String email, String bssid, String batchName, String year) {
        this.email = email;
        this.bssid = bssid;
        this.batchName = batchName;
        this.year = year;
    }

    private WifiStudentRequest() {
    }

}
