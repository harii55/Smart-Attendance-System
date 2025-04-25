package com.backend.attendance.backend.utils;

import com.backend.attendance.backend.models.Student;
import com.backend.attendance.backend.repositories.StudentRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StudentProvider {

    @Autowired
    private StudentRepository studentRepository;

    private ConcurrentHashMap<String, Student> studentDirectory = new ConcurrentHashMap<>();

    @PostConstruct
    public void LoadStudentDirectory() throws SQLException {
        studentDirectory.clear();
        List<Student> studentsList = studentRepository.getAllStudents();
        for (Student student : studentsList) {
            studentDirectory.put(student.getEmail() , student);
        }
        System.out.println("Loaded " + studentDirectory.size() + " students");
    }

    public ConcurrentHashMap<String, Student> getStudentDirectory() {
        return studentDirectory;
    }

    public void setStudentDirectory(ConcurrentHashMap<String, Student> student_directory) {
        this.studentDirectory = student_directory;
    }

}
