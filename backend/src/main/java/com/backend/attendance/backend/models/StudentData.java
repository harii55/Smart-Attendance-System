package com.backend.attendance.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "studentdata")
public class StudentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;


    public StudentData(Long id, String email) {
        this.id = id;
        this.email = email;
    }



    public StudentData() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
