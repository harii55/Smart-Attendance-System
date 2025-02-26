package com.backend.attendance.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String rollNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRollNumbeer() {
        return rollNumber;
    }

    public void setRollNumbeer(String rollNumbeer) {
        this.rollNumber = rollNumbeer;
    }
}
