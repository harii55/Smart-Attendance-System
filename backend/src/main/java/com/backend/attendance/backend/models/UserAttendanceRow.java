package com.backend.attendance.backend.models;
import java.time.LocalDate;

public class UserAttendanceRow {


        private String rollNo;
        private LocalDate attendanceDate;
        private Boolean attendanceStatus;

        // Constructors
        public UserAttendanceRow () {}

        public UserAttendanceRow (String rollNo, LocalDate attendanceDate, Boolean attendanceStatus) {
            this.rollNo = rollNo;
            this.attendanceDate = attendanceDate;
            this.attendanceStatus = attendanceStatus;
        }

        // Getters & Setters
        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public LocalDate getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(LocalDate attendanceDate) {
            this.attendanceDate = attendanceDate;
        }

        public Boolean getAttendanceStatus() {
            return attendanceStatus;
        }

        public void setAttendanceStatus(Boolean attendanceStatus) {
            this.attendanceStatus = attendanceStatus;
        }
    }

