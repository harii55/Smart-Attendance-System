package com.backend.attendance.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "studentdata")
public class StudentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private int rollNo;
    private String batch;
    private int year;

    public StudentData() {}

    public StudentData(Long id, String email, int rollNo, String batch, int year) {
        this.id = id;
        this.email = email;
        this.rollNo = rollNo;
        this.batch = batch;
        this.year = year;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
