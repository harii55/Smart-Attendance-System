package com.backend.attendance.backend.models;

public class Student {
    String email;
    String batch;
    String year;

    public Student(String email, String batch, String year) {
        this.email = email;
        this.batch = batch;
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
