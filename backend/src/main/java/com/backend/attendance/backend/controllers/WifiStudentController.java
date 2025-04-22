package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import com.backend.attendance.backend.services.WifiStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/attendance/wifi/student")
public class WifiStudentController {

    @Autowired
    private WifiStudentService wifiStudentService;

    @PostMapping("/start")
    public ResponseEntity<?> startMonitoring(@RequestBody WifiStudentRequest request) throws Exception {
        if(request.getBssid() != null && !request.getBssid().isEmpty() && request.getYear() != null && !request.getYear().isEmpty() && request.getBatchName() != null && !request.getBatchName().isEmpty() && request.getEmail() != null && !request.getEmail().isEmpty()) {
            WifiStudentResponse response = wifiStudentService.startMonitoring(request);
            if (Objects.equals(response.getMessage(), "year")){
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
            if (Objects.equals(response.getMessage(), "batch")){
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
            if (Objects.equals(response.getMessage(), "email")){
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response , HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopMonitoring(@RequestBody WifiStudentRequest request) throws Exception {
        if(request.getBssid() != null && !request.getBssid().isEmpty() && request.getYear() != null && !request.getYear().isEmpty() && request.getBatchName() != null && !request.getBatchName().isEmpty() && request.getEmail() != null && !request.getEmail().isEmpty()) {
            return ResponseEntity.ok(wifiStudentService.stopMonitoring(request));
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
