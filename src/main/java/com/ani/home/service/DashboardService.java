package com.ani.home.service;

import com.ani.home.dto.DashboardDataDTO;

import java.sql.SQLException;


public interface DashboardService {
    DashboardDataDTO getDashboardData() throws SQLException;
    DashboardDataDTO getDashboardDataForStation(int stationId);
}
