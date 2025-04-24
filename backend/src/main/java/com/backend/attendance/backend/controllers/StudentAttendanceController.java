package com.backend.attendance.backend.controllers;

import com.backend.attendance.backend.models.StudentGetAttendanceRequest;
import com.backend.attendance.backend.services.StudentGetAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/attendance/student")
public class StudentAttendanceController {

    @Autowired
    private StudentGetAttendanceService studentGetAttendanceService;

    @GetMapping
            ("/get_attendance")
    public ResponseEntity<?> getStudentAttendance(@RequestBody StudentGetAttendanceRequest request) throws SQLException {
        return studentGetAttendanceService.studentGetAttendance(request.getEmail(), request.getYear(), request.getBatch(), request.getSubject());
    }
}
