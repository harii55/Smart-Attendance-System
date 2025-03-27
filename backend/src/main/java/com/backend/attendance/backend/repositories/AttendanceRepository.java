package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import com.google.type.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Repository
public class AttendanceRepository {

    @Autowired
    private JdbcUtil jdbcUtil;

    public boolean createTable(String year,String batch,String subject) throws SQLException {

        try {
            Connection connection = jdbcUtil.createConnection();

            String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";
            String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (email VARCHAR(255) NOT NULL PRIMARY KEY)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSql);

            // TODO: FIX DUPLICATE EMAIL ENTRIES

            String selectSql = "SELECT email FROM student_directory WHERE batch = " + "\'" + batch + "\'";
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectSql);


            try {
                while (resultSet.next()) {
                    String studentEmail = resultSet.getString("email");
                    String insertSql = "INSERT INTO " + tableName + " (email) VALUES (\'" + studentEmail + "\') ";
                    Statement insertStatement = connection.createStatement();
                    insertStatement.executeUpdate(insertSql);
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
                return true;
            }


            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void storeData(HashMap<String,String> data, String batch, String subject, String year) throws SQLException {

        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";

        if(createTable(year,batch,subject)){
            DateFormat today = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = ("date_" + today.format(new Date())).replace("-" , "_");

            String q = "ALTER TABLE " + tableName + " ADD COLUMN IF NOT EXISTS \"" + todayDate + "\"  VARCHAR(255)";
            Statement statement = jdbcUtil.createConnection().createStatement();
            statement.executeUpdate(q);

            for (String key : data.keySet()) {
                String value = data.get(key);
                String q1 = "UPDATE " + tableName + " SET \"" + todayDate + "\" = ? WHERE email = ?";

                PreparedStatement ps = jdbcUtil.createConnection().prepareStatement(q1);

                if (Objects.equals(value, "P")) {
                    ps.setString(1, "P");
                    ps.setString(2, key);
                }else if (Objects.equals(value, "A")) {
                    ps.setString(1, "A");
                    ps.setString(2, key);
                }

                ps.executeUpdate();
            }

        }else{
            System.out.println("Data not saved");
        }


    }


}
