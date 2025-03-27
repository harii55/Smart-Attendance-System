package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiAdminStartRequest;
import com.backend.attendance.backend.models.WifiAdminStartResponse;
import com.backend.attendance.backend.models.WifiAdminStopRequest;
import com.backend.attendance.backend.models.WifiAdminStopResponse;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WifiAdminService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private WifiStudentService wifiStudentService;

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

    public ResponseEntity<?> stopMonitoring(@RequestBody WifiAdminStopRequest request) throws Exception {
        System.out.println("I am here");
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();

        try{
            System.out.println("I am here");
        if (monitoringStatusMap.containsKey(year) && monitoringStatusMap.get(year).containsKey(batch)) {
            HashMap<String, String> data = wifiStudentService.getFilteredAttendanceMap(year, batch, subject);
            System.out.println(data);
            System.out.println("I am here");
            attendanceRepository.storeData(data, batch, subject, year);
            return ResponseEntity.ok(new WifiAdminStopResponse("OK", "Attendance Stopped"));
        }else{
            return ResponseEntity.ok(new WifiAdminStopResponse("BAD REQUEST", "No Attendance to be stopped for current batch"));
        }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
