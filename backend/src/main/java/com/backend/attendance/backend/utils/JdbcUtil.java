package com.backend.attendance.backend.utils;

import com.google.api.client.util.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public class JdbcUtil {


//
//    @Value("${app.url}")
//    String url;
//
//    @Value("${app.username}")
//    String uname;
//
//
//    @Value("${app.password}")
//    String pass;
//

String url = "jdbc:postgresql://localhost:5432/mydatabase";
String uname = "myuser";
String pass = "mypassword";

    public Connection createConnection() throws SQLException {

        return DriverManager.getConnection(url,uname,pass);
    }


}
