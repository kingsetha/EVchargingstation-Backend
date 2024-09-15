package com.ani.home.repo;

import com.ani.home.dto.StationAdminDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepo {

    private final JdbcTemplate jdbcTemplate;

    public DashboardRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM user";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public BigDecimal getTotalAmountPaid() {
        return jdbcTemplate.queryForObject("SELECT SUM(amount) FROM payments", BigDecimal.class);
    }

    public int getNewBookings() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bookings WHERE booking_time >= CURDATE() - INTERVAL 1 MONTH", Integer.class);
    }

    public int getTotalSlots() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM slots", Integer.class);
    }

    public int getActiveSlots() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM slots WHERE status = 'active'", Integer.class);
    }

    public List<Integer> getMonthlyNewBookings() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM bookings WHERE MONTH(booking_time) = ? GROUP BY MONTH(booking_time)",
            new Object[]{1}, // Example for January; adjust as needed
            (rs, rowNum) -> rs.getInt("COUNT(*)"));
    }

    public List<Integer> getSlotAvailability() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM slots GROUP BY status",
            (rs, rowNum) -> rs.getInt("COUNT(*)"));
    }

    public List<Integer> getBookedSlotData() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM bookings GROUP BY slot_id",
            (rs, rowNum) -> rs.getInt("COUNT(*)"));
    }

////new
    
    public int getTotalUsersForStation(int stationId) {
        String sql = "SELECT COUNT(DISTINCT u.id) FROM user u " +
                     "JOIN bookings b ON u.id = b.user_id " +
                     "JOIN slots s ON b.slot_id = s.id " +
                     "WHERE s.station_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{stationId}, Integer.class);
    }


    public BigDecimal getTotalAmountPaidForStation(int stationId) {
        String sql = "SELECT SUM(p.amount) FROM payments p " +
                     "JOIN slots s ON p.slot_id = s.id " +
                     "WHERE s.station_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{stationId}, BigDecimal.class);
    }
    

    public int getNewBookingsForStation(int stationId) {
        String sql = "SELECT COUNT(*) FROM bookings b " +
                     "JOIN slots s ON b.slot_id = s.id " +
                     "WHERE s.station_id = ? AND b.booking_time >= CURDATE() - INTERVAL 1 MONTH";
        return jdbcTemplate.queryForObject(sql, new Object[]{stationId}, Integer.class);
    }

    public int getTotalSlotsForStation(int stationId) {
        String sql = "SELECT COUNT(*) FROM slots WHERE station_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{stationId}, Integer.class);
    }

    public int getActiveSlotsForStation(int stationId) {
        String sql = "SELECT COUNT(*) FROM slots WHERE station_id = ? AND status = 'active'";
        return jdbcTemplate.queryForObject(sql, new Object[]{stationId}, Integer.class);
    }

    public List<Integer> getMonthlyNewBookingsForStation(int stationId) {
        String sql = "SELECT MONTH(b.booking_time) AS month, COUNT(*) AS count " +
                     "FROM bookings b " +
                     "JOIN slots s ON b.slot_id = s.id " +
                     "WHERE s.station_id = ? AND b.booking_time >= CURDATE() - INTERVAL 12 MONTH " +
                     "GROUP BY MONTH(b.booking_time)";
        return jdbcTemplate.query(sql, new Object[]{stationId},
            (rs, rowNum) -> rs.getInt("count"));
    }

//    public List<Integer> getSlotAvailabilityForStation(int stationId) {
//        String sql = "SELECT s.status, COUNT(*) AS count FROM slots s " +
//                     "WHERE s.station_id = ? GROUP BY s.status";
//        return jdbcTemplate.query(sql, new Object[]{stationId},
//            (rs, rowNum) -> rs.getInt("count"));
//    }
    public List<Integer> getSlotAvailabilityForStation(int stationId) {
        String sql = "SELECT COUNT(*) AS count FROM slots " +
                     "WHERE station_id = ? AND status = 'AVAILABLE'";
        return jdbcTemplate.query(sql, new Object[]{stationId},
            (rs, rowNum) -> rs.getInt("count"));
    }

    public List<Integer> getBookedSlotDataForStation(int stationId) {
        String sql = "SELECT COUNT(*) AS count FROM bookings b " +
                     "JOIN slots s ON b.slot_id = s.id " +
                     "WHERE s.station_id = ? GROUP BY b.slot_id";
        return jdbcTemplate.query(sql, new Object[]{stationId},
            (rs, rowNum) -> rs.getInt("count"));
    }
    
    public List<Map<String, Object>> getMonthlyPaymentsForStation(int stationId) {
        String sql = "SELECT DATE_FORMAT(payment_date, '%Y-%m') AS period, SUM(amount) AS total_amount " +
                     "FROM payments " +
                     "WHERE station_id = ? " +
                     "GROUP BY period " +
                     "ORDER BY period";
        return jdbcTemplate.queryForList(sql, stationId);
    }


}
