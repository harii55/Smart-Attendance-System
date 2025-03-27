package com.backend.attendance.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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


}
