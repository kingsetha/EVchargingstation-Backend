package com.ani.home.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardDataDTO {

    private int totalUsers;
    private BigDecimal totalAmountPaid;
    private int newBookings;
    private int totalSlots;
    private int activeSlots;
    private List<Integer> lineChartData; // Data for the line chart
    private Map<String, List<Integer>> barChartData; // Data for the bar chart
    private List<Integer> doughnutChartData; // Data for the doughnut chart
//    private List<StationAdminDTO> nearbyStations; // Data for nearby stations

    // Constructors, getters, and setters
    public DashboardDataDTO() {
    }

    public DashboardDataDTO(int totalUsers, BigDecimal totalAmountPaid, int newBookings,
                            int totalSlots, int activeSlots, List<Integer> lineChartData,
                            Map<String, List<Integer>> barChartData, List<Integer> doughnutChartData
                           ) {
        this.totalUsers = totalUsers;
        this.totalAmountPaid = totalAmountPaid;
        this.newBookings = newBookings;
        this.totalSlots = totalSlots;
        this.activeSlots = activeSlots;
        this.lineChartData = lineChartData;
        this.barChartData = barChartData;
        this.doughnutChartData = doughnutChartData;
        
    }

    // Getters and setters
    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public int getNewBookings() {
        return newBookings;
    }

    public void setNewBookings(int newBookings) {
        this.newBookings = newBookings;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getActiveSlots() {
        return activeSlots;
    }

    public void setActiveSlots(int activeSlots) {
        this.activeSlots = activeSlots;
    }

    public List<Integer> getLineChartData() {
        return lineChartData;
    }

    public void setLineChartData(List<Integer> lineChartData) {
        this.lineChartData = lineChartData;
    }

    public Map<String, List<Integer>> getBarChartData() {
        return barChartData;
    }

    public void setBarChartData(Map<String, List<Integer>> barChartData) {
        this.barChartData = barChartData;
    }

    public List<Integer> getDoughnutChartData() {
        return doughnutChartData;
    }

    public void setDoughnutChartData(List<Integer> doughnutChartData) {
        this.doughnutChartData = doughnutChartData;
    }

}
