package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiAdminStartRequest;
import com.backend.attendance.backend.models.WifiAdminStartResponse;
import com.backend.attendance.backend.models.WifiAdminStopRequest;
import com.backend.attendance.backend.models.WifiAdminStopResponse;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WifiAdminService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private WifiStudentService wifiStudentService;

    @Setter
    @Getter
    private HashMap<String, HashMap<String, String>> monitoringStatusMap = new HashMap<>();

    public ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request){
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();

        try {

            if (monitoringStatusMap.containsKey(year)) {
                monitoringStatusMap.get(year).put(batch, subject);
            } else {
                monitoringStatusMap.put(year, new HashMap<>());
                monitoringStatusMap.get(year).put(batch, subject);
            }

            return ResponseEntity.ok(new WifiAdminStartResponse("true", "OK", 200, "Attendance Started"));

        }catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(new WifiAdminStartResponse("false", "Error", 500, "Failed to start attendance"));
        }

    }

    public ResponseEntity<?> stopMonitoring(@RequestBody WifiAdminStopRequest request) throws Exception {
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();

        try{
            if (monitoringStatusMap.containsKey(year) && monitoringStatusMap.get(year).containsKey(batch)) {
                HashMap<String, String> data = wifiStudentService.getFilteredAttendanceMap(year, batch, subject);
//                System.out.println(data); // LOG: prints current Wi-Fi attendance data of the batch...
                attendanceRepository.storeData(data, batch, subject, year);
                monitoringStatusMap.get(year).remove(batch);
                if (monitoringStatusMap.get(year).isEmpty()) {
                    monitoringStatusMap.remove(year);
                }
                return ResponseEntity.ok(new WifiAdminStopResponse("true", "OK", "Attendance stopped", 200));
            }else{
                return ResponseEntity.ok(new WifiAdminStopResponse("false", "BAD REQUEST", "No Attendance to stop", 400));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(new WifiAdminStopResponse("false", "Internal Server Error", "Something went wrong", 500));
        }
    }
}
