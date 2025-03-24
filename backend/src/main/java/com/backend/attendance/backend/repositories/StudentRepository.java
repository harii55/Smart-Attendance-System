package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class StudentRepository {
    @Autowired
    private JdbcUtil jdbcUtil;


    public Boolean lookforEmail(String email) throws SQLException {
        Connection connection = jdbcUtil.createConnection();
        String sql = "select * from student_directory where email = '" + email + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public String[] checkForBatchAndYear(String email , String batch, String year) throws SQLException {
        Connection connection = jdbcUtil.createConnection();
        String[] isPresent = new String[3]; // Here 3 values respectively mean for email , batch and year...
        String sql = "select * from student_directory where email = '" + email + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            isPresent[0] = String.valueOf(true);
            String checkBatch = rs.getString("batch");
            if (checkBatch.equals(batch)) {
                isPresent[1] = String.valueOf(true);
                String checkYear = rs.getString("year");
                if (checkYear.equals(year)) {
                    isPresent[2] = String.valueOf(true);
                    return isPresent;
                }else{
                    isPresent[2] = String.valueOf(false);
                    return isPresent;
                }
            }else{
                isPresent[1] = String.valueOf(false);
                isPresent[2] = String.valueOf(false);
                return isPresent;
            }
        }else{
            isPresent[0] = String.valueOf(false);
            isPresent[1] = String.valueOf(false);
            isPresent[2] = String.valueOf(false);
            return isPresent;
        }
    }
}
