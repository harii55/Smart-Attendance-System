package com.backend.attendance.backend.utils;

import com.google.api.client.util.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public class JdbcUtil {

    @Autowired
    private DataSource dataSource;

    public Connection createConnection() throws SQLException {

        return dataSource.getConnection();
    }

}
