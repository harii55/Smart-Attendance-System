package com.backend.attendance.backend.utils;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AttendanceProvider {

    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> monitoringStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>> attendanceMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getMonitoringStatusMap() {
        return monitoringStatusMap;
    }

    public void setMonitoringStatusMap(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> monitoringStatusMap) {
        this.monitoringStatusMap = monitoringStatusMap;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>> getAttendanceMap() {
        return attendanceMap;
    }

    public void setAttendanceMap(ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>>> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }
}
