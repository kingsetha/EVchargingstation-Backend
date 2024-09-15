package com.ani.home.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.dto.DashboardDataDTO;
import com.ani.home.dto.StationAdminDTO;
import com.ani.home.repo.DashboardRepo;
import com.ani.home.service.DashboardService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepo dashboardRepository;

    @Override
    public DashboardDataDTO getDashboardData() throws SQLException {
        int totalUsers = dashboardRepository.getTotalUsers();
        BigDecimal totalAmountPaid = dashboardRepository.getTotalAmountPaid();
        int newBookings = dashboardRepository.getNewBookings();
        int totalSlots = dashboardRepository.getTotalSlots();
        int activeSlots = dashboardRepository.getActiveSlots();
        List<Integer> monthlyNewBookings = dashboardRepository.getMonthlyNewBookings();
        List<Integer> slotAvailability = dashboardRepository.getSlotAvailability();
        List<Integer> bookedSlotData = dashboardRepository.getBookedSlotData();
        
//        List<StationAdminDTO> nearbyStations = dashboardRepository.getNearbyStations(BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060)); // Example coordinates

        Map<String, List<Integer>> barChartData = Map.of(
            "availableSlots", slotAvailability,
            "bookedSlots", bookedSlotData
        );

        List<Integer> doughnutChartData = List.of(activeSlots, totalSlots - activeSlots);

        return new DashboardDataDTO(
            totalUsers,
            totalAmountPaid,
            newBookings,
            totalSlots,
            activeSlots,
            monthlyNewBookings,
            barChartData,
            doughnutChartData
            
        );
    }

    @Override
    public DashboardDataDTO getDashboardDataForStation(int stationId) {
        int totalUsers = dashboardRepository.getTotalUsersForStation(stationId);
        BigDecimal totalAmountPaid = dashboardRepository.getTotalAmountPaidForStation(stationId);
        int newBookings = dashboardRepository.getNewBookingsForStation(stationId);
            int totalSlots = dashboardRepository.getTotalSlotsForStation(stationId);
        int activeSlots = dashboardRepository.getActiveSlotsForStation(stationId);
        List<Integer> monthlyNewBookings = dashboardRepository.getMonthlyNewBookingsForStation(stationId);
        List<Integer> slotAvailability = dashboardRepository.getSlotAvailabilityForStation(stationId);
        List<Integer> bookedSlotData = dashboardRepository.getBookedSlotDataForStation(stationId);

        Map<String, List<Integer>> barChartData = Map.of(
            "availableSlots", slotAvailability,
            "bookedSlots", bookedSlotData
        );

        List<Integer> doughnutChartData = List.of(activeSlots, totalSlots - activeSlots);

        return new DashboardDataDTO(
            totalUsers,
            totalAmountPaid,
            newBookings,
            totalSlots,
            activeSlots,
            monthlyNewBookings,
            barChartData,
            doughnutChartData
        );
    }
}
