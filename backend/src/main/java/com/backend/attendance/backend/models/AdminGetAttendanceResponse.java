package com.backend.attendance.backend.models;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminGetAttendanceResponse {
    String year;
    String batch;
    String subject;

    String[] columnNames;
    HashMap<String, ArrayList<String>> studentAttendance;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public HashMap<String, ArrayList<String>> getStudentAttendance() {
        return studentAttendance;
    }

    public void setStudentAttendance(HashMap<String, ArrayList<String>> studentAttendance) {
        this.studentAttendance = studentAttendance;
    }
}
