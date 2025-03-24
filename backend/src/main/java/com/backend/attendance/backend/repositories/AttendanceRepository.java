package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


@Repository
public class AttendanceRepository {

    @Autowired
    private JdbcUtil jdbcUtil;

    public boolean createTable(String year,String batch,String subject) throws SQLException {

        try {

            Connection connection = jdbcUtil.createConnection();
            Statement statement = connection.createStatement();


            String sql = "create table " + year + "_" + batch + "_" + subject + "_WIFI_ATTENDANCE_TABLE(" +
                    "email varchar(255))";

            statement.executeUpdate(sql);

            return true;

        }catch (Exception e){
            return false;
        }
    }

    public void storeData(HashMap<String,String> data, String batch, String subject, String year) throws SQLException {

        createTable(year,batch,subject);

        if(createTable(year,batch,subject)){

            System.out.println("Data saved");

        }else{
            System.out.println("Data not saved");
        }


    }


}
