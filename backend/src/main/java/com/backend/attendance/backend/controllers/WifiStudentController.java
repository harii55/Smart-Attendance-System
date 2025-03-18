package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import com.backend.attendance.backend.services.WifiStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance/wifi/student")
public class WifiStudentController {

    @Autowired
    private WifiStudentService wifiStudentService;

    @PostMapping("/start")
    public ResponseEntity<?> startMonitoring(@RequestBody WifiStudentRequest request) throws Exception {
        if(request.getIpAddress() != null && !request.getIpAddress().isEmpty() && request.getYear() != null && !request.getYear().isEmpty() && request.getBatchName() != null && !request.getBatchName().isEmpty() && request.getEmail() != null && !request.getEmail().isEmpty()) {
            return ResponseEntity.ok(wifiStudentService.startMonitoring(request));
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopMonitoring(@RequestBody WifiStudentRequest request) throws Exception {
        if(request.getIpAddress() != null && !request.getIpAddress().isEmpty() && request.getYear() != null && !request.getYear().isEmpty() && request.getBatchName() != null && !request.getBatchName().isEmpty() && request.getEmail() != null && !request.getEmail().isEmpty()) {
            return ResponseEntity.ok(wifiStudentService.stopMonitoring(request));
        }else{
            return ResponseEntity.badRequest().build();
        }
    }



}
