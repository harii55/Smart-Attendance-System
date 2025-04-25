package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.models.Student;
import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    @Autowired
    private JdbcUtil jdbcUtil;

    public List<Student> getAllStudents() throws SQLException{

        List<Student> students = new ArrayList<>();
        String query = "SELECT email, batch, year FROM student_directory";

        try{
            Connection conn = jdbcUtil.createConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                students.add(new Student(rs.getString("email"), rs.getString("batch") , rs.getString("year")));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return students;
    }
}
