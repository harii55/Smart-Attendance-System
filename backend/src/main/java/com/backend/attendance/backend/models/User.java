package com.backend.attendance.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students")
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String password;
    private String rollNumber;

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumbeer(String rollNumbeer) {
        this.rollNumber = rollNumbeer;
    }
}
