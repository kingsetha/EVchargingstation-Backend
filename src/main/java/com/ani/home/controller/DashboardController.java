package com.ani.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ani.home.dto.DashboardDataDTO;
import com.ani.home.service.DashboardService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

   
    public DashboardController(DashboardService dashboardService) {
		super();
		this.dashboardService = dashboardService;
	}
	private DashboardService dashboardService;

    @GetMapping("/data")
    public ResponseEntity<DashboardDataDTO> getDashboardData() {
        try {
            DashboardDataDTO dashboardData = dashboardService.getDashboardData();
            return ResponseEntity.ok(dashboardData);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/data/station/{stationId}")
    public ResponseEntity<DashboardDataDTO> getDashboardDataForStation(@PathVariable int stationId) {
        DashboardDataDTO dashboardData = dashboardService.getDashboardDataForStation(stationId);
		return ResponseEntity.ok(dashboardData);
    }
}
