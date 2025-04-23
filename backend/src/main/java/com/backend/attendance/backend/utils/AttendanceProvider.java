package com.backend.attendance.backend.utils;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AttendanceProvider {

    private ConcurrentHashMap<String, Long> monitoringStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> subjectMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Long> getMonitoringStatusMap() {
        return monitoringStatusMap;
    }

    public void setMonitoringStatusMap(ConcurrentHashMap<String, Long> monitoringStatusMap) {
        this.monitoringStatusMap = monitoringStatusMap;
    }

    public ConcurrentHashMap<String, String> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(ConcurrentHashMap<String, String> subjectMap) {
        this.subjectMap = subjectMap;
    }
}
