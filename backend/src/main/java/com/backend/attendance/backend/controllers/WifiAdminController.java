package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.*;
import com.backend.attendance.backend.services.WifiAdminService;
import com.backend.attendance.backend.utils.StudentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/attendance/wifi/admin")
public class WifiAdminController {

    @Autowired
    private WifiAdminService wifiAdminService;
    @Autowired
    private StudentProvider studentProvider;


    @PostMapping("/start")
    ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
        return wifiAdminService.startMonitoring(request);
    }

    @PostMapping("/stop")
    ResponseEntity<?> stopMonitoring(@RequestBody WifiAdminStopRequest request) throws  Exception{
        return wifiAdminService.stopMonitoring(request);
    }

    @PostMapping("/cache/refresh")
    ResponseEntity<?> refreshCache() {
        try {
            studentProvider.LoadStudentDirectory();
            return ResponseEntity.ok().body("Student directory refreshed");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student directory refresh failed due to : " + e.getMessage());
        }
    }
}
