package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.*;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import com.backend.attendance.backend.repositories.StudentRepository;
import com.backend.attendance.backend.utils.AttendanceProvider;
import com.backend.attendance.backend.utils.StudentProvider;
import com.backend.attendance.backend.websockets.SocketConnectionHandler;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WifiAdminService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private SocketConnectionHandler socketConnectionHandler;

    @Autowired
    private AttendanceProvider attendanceProvider;
    @Autowired
    private StudentProvider studentProvider;

    ConcurrentHashMap<String, Long> monitoringStatusMap;
    ConcurrentHashMap<String, String> subjectMap;

    @PostConstruct
    public void init() {
        this.monitoringStatusMap = attendanceProvider.getMonitoringStatusMap();
        this.subjectMap = attendanceProvider.getSubjectMap();
    }

    public ResponseEntity<?> startMonitoring(@RequestBody WifiAdminStartRequest request){
        String year = request.getYear();
        String batch = request.getBatch();
        String subject = request.getSubject();
        String attendanceStatus = year + ":" + batch + ":" + subject;
        String subjectKey = year + ":" + batch;
        try {
            if (!subjectMap.containsKey(subjectKey)) {
                subjectMap.put(subjectKey, subject);
            }
            if (!monitoringStatusMap.containsKey(attendanceStatus)) {
                monitoringStatusMap.put(attendanceStatus, System.currentTimeMillis());
            }
            attendanceProvider.setMonitoringStatusMap(monitoringStatusMap);
            attendanceProvider.setSubjectMap(subjectMap);
            System.out.println(monitoringStatusMap);
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
        String attendanceStatus = year + ":" + batch + ":" + subject;

        try{
            if(!monitoringStatusMap.containsKey(attendanceStatus)) {
                return ResponseEntity.ok(new WifiAdminStopResponse("false" , "BAD REQUEST" , "No attendance to stop" , 400));
            }

            Long startTime = monitoringStatusMap.get(attendanceStatus);
            Long stopTime = System.currentTimeMillis();
            Long classDuration = stopTime - startTime;

            ConcurrentHashMap<String, StudentSession> filteredStudentSessionMap = socketConnectionHandler.getStudentSessionMap(year, batch, subject);
            ConcurrentHashMap<String, String> finalAttendanceMap = new ConcurrentHashMap<>();
            double attendanceThreshold = 0.75;

            for (StudentSession s : filteredStudentSessionMap.values()) {
                long now = System.currentTimeMillis();
                long endingTimePart = now - s.getLastPingTime();
                long totalConnectionTime = s.getTotalConnectionTime() + endingTimePart;

                if (totalConnectionTime >= attendanceThreshold * classDuration) {
                    finalAttendanceMap.put(s.getEmail(), "P");
                }else{
                    finalAttendanceMap.put(s.getEmail(), "A");
                }

            }
            attendanceRepository.storeData(finalAttendanceMap, batch , subject, year);
            socketConnectionHandler.stopMonitoring(year, batch, subject);
            monitoringStatusMap.remove(attendanceStatus);
            attendanceProvider.setMonitoringStatusMap(monitoringStatusMap);
            return ResponseEntity.ok(new WifiAdminStopResponse("true", "OK", "Attendance Stored", 200));
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(new WifiAdminStopResponse("false", "Internal Server Error", "Something went wrong", 500));
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshCache() throws SQLException {
        try{
            studentProvider.LoadStudentDirectory();
            System.out.println("Student Directory cache refreshed");
        }catch (Exception e) {
            System.err.println("Student cache refresh failed : " + e.getMessage());
        }
    }
}
