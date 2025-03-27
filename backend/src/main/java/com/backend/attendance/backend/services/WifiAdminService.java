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

@Service
public class WifiAdminService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private WifiStudentService wifiStudentService;

    @Setter
    @Getter
    private HashMap<String, HashMap<String, ArrayList<String>>> monitoringStatusMap = new HashMap<>();

    public ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request) throws Exception {
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
            monitoringStatusMap.put(year,monitoringStatusInnerMap);

            return ResponseEntity.ok(new WifiAdminStartResponse("true"));
        }else{
            monitoringStatusMap.get(year).remove(batch);
            return ResponseEntity.ok(new WifiAdminStartResponse("false"));
        }
    }

    public ResponseEntity<?> stopMonitoring(@RequestBody WifiAdminStopRequest request) throws Exception {
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();

        try{
            if (monitoringStatusMap.containsKey(year) && monitoringStatusMap.get(year).containsKey(batch)) {
                HashMap<String, String> data = wifiStudentService.getFilteredAttendanceMap(year, batch, subject);
                System.out.println(data);
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
