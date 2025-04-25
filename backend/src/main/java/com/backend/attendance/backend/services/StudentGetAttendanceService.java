package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.StudentGetAttendanceRequest;
import com.backend.attendance.backend.models.StudentGetAttendanceResponse;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import com.backend.attendance.backend.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

@Service
public class StudentGetAttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public ResponseEntity<?> studentGetAttendance(String email, String year, String batch, String subject) throws SQLException {
        if (email == null || year == null || batch == null || subject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        StudentGetAttendanceResponse studentGetAttendanceResponse = new StudentGetAttendanceResponse();
        studentGetAttendanceResponse.setEmail(email);
        studentGetAttendanceResponse.setYear(year);
        studentGetAttendanceResponse.setBatch(batch);
        studentGetAttendanceResponse.setSubject(subject);

        ResultSet studentAttendance = attendanceRepository.getStudentAttendance(email, year, batch, subject);
        ResultSetMetaData metaData = studentAttendance.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        int totalAttendance = 0;
        int totalPresent = 0;
        int totalAbsent = 0;
        for(int i = 1; i < columnNames.length; i++) {
            totalAttendance++;
            if (studentAttendance.next()) {
                if (studentAttendance.getString(columnNames[i]).equals("P")) {
                    totalPresent++;
                } else {
                    totalAbsent++;
                }
            }
        }

        studentGetAttendanceResponse.setTotalClasses(String.valueOf(totalAttendance));
        studentGetAttendanceResponse.setTotalPresent(String.valueOf(totalPresent));
        studentGetAttendanceResponse.setTotalAbsent(String.valueOf(totalAbsent));


        if (studentAttendance != null) {
            return ResponseEntity.status(HttpStatus.OK).body(studentGetAttendanceResponse);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
