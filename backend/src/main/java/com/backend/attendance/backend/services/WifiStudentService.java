package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.WifiStudentRequest;
import com.backend.attendance.backend.models.WifiStudentResponse;
import com.backend.attendance.backend.repositories.AccessPointRepository;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import com.backend.attendance.backend.repositories.StudentRepository;
import com.backend.attendance.backend.utils.AttendanceProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WifiStudentService {

    @Autowired
    private StudentRepository studentDirectory;

    @Autowired
    private AccessPointRepository accessPointDirectory;

    @Autowired
    private AttendanceProvider attendanceProvider;

    ConcurrentHashMap<String, ConcurrentHashMap<String, String>> monitoringStatusMap;
    ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>> attendanceMap;

    @PostConstruct
    public void init() {
        this.attendanceMap = attendanceProvider.getAttendanceMap();
    }

    public WifiStudentResponse startMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        this.monitoringStatusMap = attendanceProvider.getMonitoringStatusMap();

        String bssid = wifiMonitoringRequest.getBssid();
        String batch = wifiMonitoringRequest.getBatchName();
        String email = wifiMonitoringRequest.getEmail();
        String year = wifiMonitoringRequest.getYear();

        if (bssid == null || batch == null || email == null || year == null) {
            return new WifiStudentResponse("BAD REQUEST", "Invalid Body parameters", 400);
        }

        String[] isValidRequestCheck = studentDirectory.checkForBatchAndYear(email, batch, year);

        if (isValidRequestCheck[0].equals("false")) {
            return new WifiStudentResponse("BAD REQUEST", "email", 400);
        }

        if (isValidRequestCheck[1].equals("false")) {
            return new WifiStudentResponse("BAD REQUEST", "batch", 400);
        }

        if (isValidRequestCheck[2].equals("false")) {
            return new WifiStudentResponse("BAD REQUEST", "year", 400);
        }
        if (!accessPointDirectory.checkAccessPoint(bssid)) {
            return new WifiStudentResponse("BAD REQUEST", "Not on Correct Wifi", 400);
        }

        try {
            System.out.println(monitoringStatusMap);
            if (monitoringStatusMap != null && !monitoringStatusMap.isEmpty() && monitoringStatusMap.containsKey(year)) {
                String subject = monitoringStatusMap.get(year).get(batch).toString();
                    if (subject != null) {
                        if (attendanceMap.containsKey(year)) {
                            if (attendanceMap.get(year).containsKey(batch)) {
                                if (attendanceMap.get(year).get(batch).containsKey(subject)) {
                                    attendanceMap.get(year).get(batch).get(subject).put(email, "P");
                                } else {
                                    ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
                                    data.put(email, "P");
                                    attendanceMap.get(year).get(batch).put(subject, data);
                                }
                            } else {
                                ConcurrentHashMap<String, ConcurrentHashMap<String, String>> data = new ConcurrentHashMap<>();
                                ConcurrentHashMap<String, String> attendanceOfStudent = new ConcurrentHashMap<>();
                                attendanceOfStudent.put(email, "P");
                                data.put(subject, attendanceOfStudent);
                                attendanceMap.get(year).put(batch, data);
                            }
                        } else {
                            ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> data = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, ConcurrentHashMap<String, String>> batchWithAttendance = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> attendanceOfStudent = new ConcurrentHashMap<>();
                            attendanceOfStudent.put(email, "P");
                            batchWithAttendance.put(subject, attendanceOfStudent);
                            data.put(batch, batchWithAttendance);
                            attendanceMap.put(year, data);
                        }
                    }
                    attendanceProvider.setAttendanceMap(attendanceMap);
                    return new WifiStudentResponse("OK", "Attendance marked successfully", 200);
                } else {
                    return new WifiStudentResponse("BAD REQUEST", "Attendance Not Started Yet", 400);
                }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new WifiStudentResponse("ERROR", "Internal Server Error", 500);
        }

    }

    public WifiStudentResponse stopMonitoring(@RequestBody WifiStudentRequest wifiMonitoringRequest) throws Exception {
        return new WifiStudentResponse("OK", "Attendance marked successfully", 200);
    }

    public ConcurrentHashMap<String, String> getFilteredAttendanceMap(String year, String batch, String subject) {
        if (attendanceMap.containsKey(year) && attendanceMap.get(year).containsKey(batch)
                && attendanceMap.get(year).get(batch).containsKey(subject)) {
            return attendanceMap.get(year).get(batch).get(subject);
        } else {
            return new ConcurrentHashMap<>();
        }
    }

}
