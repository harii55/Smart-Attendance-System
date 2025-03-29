package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import com.backend.attendance.backend.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WifiStudentService {
    @Autowired
    private WifiAdminService wifiAdminService;

    @Autowired
    private StudentRepository studentDirectory;

    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> attendanceMap = new HashMap<>();

    public WifiStudentResponse startMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {

        String ipAddress = wifiMonitoringRequest.getIpAddress();
        String batch = wifiMonitoringRequest.getBatchName();
        String email = wifiMonitoringRequest.getEmail();
        String year = wifiMonitoringRequest.getYear();

        String[] isValidRequestCheck = studentDirectory.checkForBatchAndYear(email, batch, year);

        if (isValidRequestCheck[0].equals("false")) {
            return new WifiStudentResponse("192.168.0.0" , "BAD REQUEST" , "email", 400);
        }

        if (isValidRequestCheck[1].equals("false")) {
            return new WifiStudentResponse("192.168.0.0" , "BAD REQUEST" , "batch", 400);
        }

        if (isValidRequestCheck[2].equals("false")) {
            return new WifiStudentResponse("192.168.0.0" , "BAD REQUEST" , "year", 400);
        }

        try{
//            TODO: Check existence of ip in range... (later)
            HashMap<?,?> monitoringStatusMap = wifiAdminService.getMonitoringStatusMap().get(year);

            if(monitoringStatusMap != null && !monitoringStatusMap.isEmpty() && monitoringStatusMap.containsKey(batch)){
                String subject = monitoringStatusMap.get(batch).toString();
                if(subject != null){
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
            }else{
                System.out.println("Attendance Not Found");
            }
            return new WifiStudentResponse("192.168.0.0" , "OK", "Attendance marked successfully", 200);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new WifiStudentResponse("192.168.0.0" , "ERROR" , "Internal Server Error", 500);
        }

    }

    public WifiStudentResponse stopMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        return new WifiStudentResponse("192.168.0.0" , "OK", "Attendance marked successfully", 200);
    }

    public HashMap<String, String> getFilteredAttendanceMap(String year, String batch, String subject) {
        if (attendanceMap.containsKey(year) && attendanceMap.get(year).containsKey(batch) && attendanceMap.get(year).get(batch).containsKey(subject)) {
            return attendanceMap.get(year).get(batch).get(subject);
        }else{
            return new HashMap<>();
        }
    }

}
