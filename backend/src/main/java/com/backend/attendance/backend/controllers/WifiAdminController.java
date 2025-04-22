package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.*;
import com.backend.attendance.backend.services.WifiAdminService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/start")
    ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
        return wifiAdminService.startMonitoring(request);
    }

    @PostMapping("/stop")
    ResponseEntity<?> stopMonitoring(@RequestBody WifiAdminStopRequest request) throws  Exception{
        return wifiAdminService.stopMonitoring(request);
    }
}
