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

    public WifiAdminStartResponse startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();
        String status = request.getMonitoring().toString();

        if (request.getMonitoring()) {

            HashMap<String, ArrayList<String>> monitoringStatusInnerMap = new HashMap<>();
            ArrayList<String> monitoringStatusList = new ArrayList<>();

            monitoringStatusList.add(0,subject);
            monitoringStatusList.add(1,status);

            monitoringStatusInnerMap.put(batch,monitoringStatusList);

            if (monitoringStatusMap.containsKey(year)) {
                monitoringStatusMap.get(year).put(batch,monitoringStatusList);
            }else {
                monitoringStatusMap.put(year, monitoringStatusInnerMap);
            }


            return new WifiAdminStartResponse("true");
        }else{
            monitoringStatusMap.get(year).remove(batch);

            return new WifiAdminStartResponse("false");
        }
    }

}
