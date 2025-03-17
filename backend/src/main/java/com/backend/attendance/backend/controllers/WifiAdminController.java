package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.WifiAdminStartRequest;
import com.backend.attendance.backend.models.WifiAdminStartResponse;
import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance/wifi/admin")
public class WifiAdminController {

    @PostMapping("/start")
    public ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
        if (request.getMonitoring()) {
            return ResponseEntity.ok(new WifiAdminStartResponse("true"));
        }else{
            return ResponseEntity.ok(new WifiAdminStartResponse("false"));
        }

    }

}
