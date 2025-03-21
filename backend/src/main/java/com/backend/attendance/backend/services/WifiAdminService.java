package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiAdminStartRequest;
import com.backend.attendance.backend.models.WifiAdminStartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WifiAdminService {
    private HashMap<String, HashMap<String, ArrayList<String>>> monitoringStatusMap = new HashMap<>();

    public HashMap<String, HashMap<String, ArrayList<String>>> getMonitoringStatusMap() {
        return monitoringStatusMap;
    }

    public void setMonitoringStatusMap(HashMap<String, HashMap<String, ArrayList<String>>> monitoringStatusMap) {
        this.monitoringStatusMap = monitoringStatusMap;
    }

    public ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();
        String status = request.getMonitoring().toString();

        if (request.getMonitoring()) {
            System.out.println("Starting Monitoring");


            HashMap<String, ArrayList<String>> monitoringStatusInnerMap = new HashMap<>();
            ArrayList<String> monitoringStatusList = new ArrayList<>();

            monitoringStatusList.add(0,subject);
            monitoringStatusList.add(1,status);

            monitoringStatusInnerMap.put(batch,monitoringStatusList);
            monitoringStatusMap.put(year,monitoringStatusInnerMap);


            return ResponseEntity.ok(new WifiAdminStartResponse("true"));
        }else{
            System.out.println("Stop Monitoring");
            monitoringStatusMap.get(year).remove(batch);

            return ResponseEntity.ok(new WifiAdminStartResponse("false"));
        }
    }

}
