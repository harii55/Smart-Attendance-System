package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AttendanceRepository {

    @Autowired
    private JdbcUtil jdbcUtil;

    public JdbcUtil getJdbcUtil() {
        return jdbcUtil;
    }

    public boolean createTable(String year, String batch, String subject) {
        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";
        try (Connection connection = jdbcUtil.createConnection();
             Statement statement = connection.createStatement();
             Statement selectStatement = connection.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (email VARCHAR(255) NOT NULL PRIMARY KEY)";
            statement.executeUpdate(createTableSql);

            String selectSql = "SELECT email FROM student_directory WHERE batch = '" + batch + "'";
            ResultSet resultSet = selectStatement.executeQuery(selectSql);

            while (resultSet.next()) {
                String studentEmail = resultSet.getString("email");
                try (PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO " + tableName + " (email) VALUES (?) ON CONFLICT DO NOTHING")) {
                    insertStatement.setString(1, studentEmail);
                    insertStatement.executeUpdate();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void storeData(ConcurrentHashMap<String, String> data, String batch, String subject, String year) {
        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";

        if (createTable(year, batch, subject)) {
            DateFormat today = new SimpleDateFormat("dd-MM-yyyy");
            String todayDate = ("date_" + today.format(new Date())).replace("-", "_");

            try (Connection connection = jdbcUtil.createConnection();
                 Statement statement = connection.createStatement()) {

                String q = "ALTER TABLE " + tableName + " ADD COLUMN IF NOT EXISTS \"" + todayDate + "\"  VARCHAR(255)";
                statement.executeUpdate(q);

                for (String key : data.keySet()) {
                    String value = data.get(key);
                    String q1 = "UPDATE " + tableName + " SET \"" + todayDate + "\" = ? WHERE email = ?";

                    try (PreparedStatement ps = connection.prepareStatement(q1)) {
                        ps.setString(1, Objects.equals(value, "P") ? "P" : "A");
                        ps.setString(2, key);
                        ps.executeUpdate();
                    }
                }

                // Mark any remaining NULLs as "A"
                String markAbsentSql = "UPDATE " + tableName + " SET \"" + todayDate + "\" = 'A' WHERE \"" + todayDate + "\" IS NULL";
                statement.executeUpdate(markAbsentSql);

            } catch (SQLException e) {
                System.out.println("Error in storeData: " + e.getMessage());
            }
        } else {
            System.out.println("Data not saved");
        }
    }

    public ResultSet getStudentAttendance(String email, String year, String batch, String subject) throws SQLException {
        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";
        String query = "SELECT * FROM " + tableName + " WHERE email = '" + email + "'";

        Connection connection = jdbcUtil.createConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query); // caller must close connection, statement, resultSet
    }

    public ResultSet getAllAttendance(String year, String batch, String subject) throws SQLException {
        String tableName = subject + "_" + batch + "_" + year + "_WIFI_ATTENDANCE_TABLE";
        String query = "SELECT * FROM " + tableName;

        Connection connection = jdbcUtil.createConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query); // caller must close connection, statement, resultSet
    }
}
