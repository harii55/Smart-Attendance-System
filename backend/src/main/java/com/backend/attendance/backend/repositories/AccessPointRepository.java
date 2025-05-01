package com.backend.attendance.backend.repositories;

import com.backend.attendance.backend.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Repository
public class AccessPointRepository {

    @Autowired
    private JdbcUtil jdbcUtil;

    private final HashSet<String> cachedBssids = new HashSet<>();

    @PostConstruct
    public void loadAccessPoints() throws SQLException {
        Connection conn = jdbcUtil.createConnection();
        String sql = "SELECT mac_address FROM access_points";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            cachedBssids.add(rs.getString("mac_address"));
        }
        System.out.println("✅ Access points loaded: " + cachedBssids.size());
        conn.close();
    }

    public boolean checkAccessPoint(String bssid) {
        return cachedBssids.contains(bssid);
    }
}

