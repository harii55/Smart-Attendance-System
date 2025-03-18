package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import com.google.firebase.database.snapshot.StringNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WifiStudentService {
    @Autowired
    private WifiAdminService wifiAdminService;

    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> attendanceMap = new HashMap<>();

    public WifiStudentResponse startMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {

        String ipAddress = wifiMonitoringRequest.getIpAddress();
        String batch = wifiMonitoringRequest.getBatchName();
        String email = wifiMonitoringRequest.getEmail();
        String year = wifiMonitoringRequest.getYear();

        try{
//            TODO: Check existence of ip in range... (later)
            HashMap<?,?> monitoringStatusMap = wifiAdminService.getMonitoringStatusMap().get(year);

            if(monitoringStatusMap != null){
                ArrayList<String> statusList = (ArrayList<String>) monitoringStatusMap.get(batch);
                if(statusList != null){
                    String subject = statusList.get(0);
                    String status = statusList.get(1);

                    if (status != null && status.equals("true")) {

                        if (attendanceMap.containsKey(year)){
                            if (attendanceMap.get(year).containsKey(batch)){
                                if (attendanceMap.get(year).get(batch).containsKey(subject)){
                                    attendanceMap.get(year).get(batch).get(subject).put(email, "P");
                                }else{
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put(email, "P");
                                    attendanceMap.get(year).get(batch).put(subject, data);
                                }
                            }else{
                                HashMap<String, HashMap<String, String>> data = new HashMap<>();
                                HashMap<String, String> attendanceOfStudent = new HashMap<>();
                                attendanceOfStudent.put(email, "P");
                                data.put(subject, attendanceOfStudent);
                                attendanceMap.get(year).put(batch, data);
                            }
                        }else{
                            HashMap<String, HashMap<String, HashMap<String, String>>> data = new HashMap<>();
                            HashMap<String, HashMap<String, String>> batchWithAttendance = new HashMap<>();
                            HashMap<String, String> attendanceOfStudent = new HashMap<>();
                            attendanceOfStudent.put(email, "P");
                            batchWithAttendance.put(subject, attendanceOfStudent);
                            data.put(batch, batchWithAttendance);
                            attendanceMap.put(year, data);
                        }
                    }
                }
            }else{
                System.out.println("Attendance Not Found");
            }

            System.out.println(attendanceMap);

        }catch (Exception e){
            e.printStackTrace();
        }

        return new WifiStudentResponse("192.168.0.0" , "true");
    }

    public WifiStudentResponse stopMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        return new WifiStudentResponse("192.168.0.0" , "false");
    }

}
