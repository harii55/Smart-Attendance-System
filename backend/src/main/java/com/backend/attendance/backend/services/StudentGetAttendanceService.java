package com.backend.attendance.backend.services;

import com.backend.attendance.backend.models.StudentGetAttendanceResponse;
import com.backend.attendance.backend.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentGetAttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public ResponseEntity<?> studentGetAttendance(String email, String year, String batch, String subject) {
        if (email == null || year == null || batch == null || subject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        StudentGetAttendanceResponse response = new StudentGetAttendanceResponse();
        response.setEmail(email);
        response.setYear(year);
        response.setBatch(batch);
        response.setSubject(subject);

        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";
        String query = "SELECT * FROM " + tableName + " WHERE email = ?";

        try (Connection conn = attendanceRepository.getJdbcUtil().createConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                int totalClasses = 0;
                int totalPresent = 0;
                int totalAbsent = 0;

                for (int i = 2; i <= columnCount; i++) { // start from 2 (skip email column)
                    String attendance = rs.getString(i);
                    if (attendance != null) {
                        totalClasses++;
                        if (attendance.equals("P")) {
                            totalPresent++;
                        } else {
                            totalAbsent++;
                        }
                    }
                }

                response.setTotalClasses(String.valueOf(totalClasses));
                response.setTotalPresent(String.valueOf(totalPresent));
                response.setTotalAbsent(String.valueOf(totalAbsent));
            }

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB Error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
