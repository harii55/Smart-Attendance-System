package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AccessPointRepository {

    @Autowired
    private JdbcUtil jdbcUtil;

    public boolean checkAccessPoint(String bssid) throws SQLException {
        Connection conn = jdbcUtil.createConnection();
        String sql = "select * from access_points where mac_address = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, bssid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }else{
            return false;
        }
    }
}
