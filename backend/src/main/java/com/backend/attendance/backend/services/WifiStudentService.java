package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class WifiStudentService {

    @PostMapping("/start")
    public ResponseEntity<?> startMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        return ResponseEntity.ok(new WifiStudentResponse("192.168.0.0" , "true"));
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        return ResponseEntity.ok(new WifiStudentResponse("192.168.0.0" , "false"));
    }

}
