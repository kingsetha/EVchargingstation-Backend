package com.ani.home.service;

import com.ani.home.dto.DashboardDataDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface DashboardService {
    DashboardDataDTO getDashboardData() throws SQLException;
    DashboardDataDTO getDashboardDataForStation(int stationId);
}
