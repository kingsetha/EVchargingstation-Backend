
package com.ani.home.repoimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ani.home.model.Booking.DeviceType;
import com.ani.home.model.Slot;
import com.ani.home.model.Slot.ChargingType;
import com.ani.home.model.Slot.SlotStatus;
import com.ani.home.model.User;
import com.ani.home.repo.SlotRepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class SlotRepoImpl implements SlotRepo {

    @PersistenceContext
    private EntityManager em;
    
    private static final String URL = "jdbc:mysql://localhost:3306/evani_db?createDatabaseIfNotExist=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Admin@123";

    @Override
    public Slot save(Slot slot) {
        if (slot != null) {
            if (slot.getId() != 0) {
                // Update existing slot
                return em.merge(slot);
            } else {
                // Insert new slot
                em.persist(slot);
                return slot; // Return the entity after persisting
            }
        } else {
            throw new IllegalArgumentException("Slot must not be null");
        }
    }

    @Override
    public void deleteById(int id) {
        Slot slot = em.find(Slot.class, id);
        if (slot != null) {
            em.remove(slot);
        } else {
            throw new RuntimeException("Slot not found with id: " + id);
        }
    }

    @Override
    public Optional<Slot> findById(int id) {
        Slot slot = em.find(Slot.class, id);
        return Optional.ofNullable(slot);
    }
    

    @Override
    public List<Slot> findAll() {
        String hql = "FROM Slot";
        return em.createQuery(hql, Slot.class).getResultList();
    }

    @Override
    public List<Slot> findByStationId(String stationId) {
        String jpql = "SELECT s FROM Slot s WHERE s.chargingStation.id = :stationId";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("stationId", stationId);
        return query.getResultList();
    }

//    @Override
//    public List<Slot> findByStationIdNew(int stationId) {
//        List<Slot> slots = new ArrayList<>();
//        String query = "SELECT * FROM slots WHERE station_id = ?";
//
//        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, stationId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    Slot slot = new Slot();
//                    slot.setId(resultSet.getInt("id"));
//                    
//                    // Retrieve and set start time and end time as strings
//                    slot.setStartTime(resultSet.getString("start_time"));
//                    slot.setEndTime(resultSet.getString("end_time"));
//                    
//                    // Retrieve and set charging type and status
//                    slot.setChargingType(ChargingType.valueOf(resultSet.getString("charging_type")));
//                    slot.setStatus(SlotStatus.valueOf(resultSet.getString("status")));
//                    
//                    // Retrieve and set counts
//                    slot.setLevel1Count(resultSet.getInt("level1_count"));
//                    slot.setLevel2Count(resultSet.getInt("level2_count"));
//                    slot.setDcFastChargingCount(resultSet.getInt("dc_fast_charging_count"));
//                    
//                    // Retrieve and set created at and updated at as strings
//                    slot.setCreatedAt(resultSet.getString("created_at"));
//                    slot.setUpdatedAt(resultSet.getString("updated_at"));
//
//                    slots.add(slot);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Database operation failed", e);
//        }
//
//        return slots;
//    }

    @Override
    public List<Slot> findByStationIdNew(int stationId) {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT s.id AS slot_id, s.start_time, s.end_time, s.charging_type, s.status, " +
                "s.level1_count, s.level2_count, s.dc_fast_charging_count, s.created_at, s.updated_at, " +
                "b.user_id, u.username, u.email " +
                "FROM slots s " +
                "LEFT JOIN bookings b ON s.id = b.slot_id " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "WHERE s.station_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, stationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Slot slot = new Slot();
                    slot.setId(resultSet.getInt("slot_id"));
                    slot.setStartTime(resultSet.getString("start_time"));
                    slot.setEndTime(resultSet.getString("end_time"));
                    slot.setChargingType(Slot.ChargingType.valueOf(resultSet.getString("charging_type")));
                    slot.setStatus(Slot.SlotStatus.valueOf(resultSet.getString("status")));
                    slot.setLevel1Count(resultSet.getInt("level1_count"));
                    slot.setLevel2Count(resultSet.getInt("level2_count"));
                    slot.setDcFastChargingCount(resultSet.getInt("dc_fast_charging_count"));
                    slot.setCreatedAt(resultSet.getString("created_at"));
                    slot.setUpdatedAt(resultSet.getString("updated_at"));

                    // Handle user information
                    User user = null;
                    int userId = resultSet.getInt("user_id");
                    if (userId > 0) {
                        user = new User();
                        user.setId(userId);
                        user.setFullName(resultSet.getString("username")); // Adjust field names as necessary
                        user.setEmail(resultSet.getString("email"));
                    }

                    // If Slot has a field for user details
                    // slot.setUser(user); // This requires Slot to have a user field.

                    // Add slot to the list
                    slots.add(slot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }

        return slots;
    }


    @Override
    public Slot findAvailableSlotByChargingType(ChargingType chargingType) {
        String jpql = "SELECT s FROM Slot s WHERE s.status = :status AND s.chargingType = :chargingType";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("status", SlotStatus.AVAILABLE);
        query.setParameter("chargingType", chargingType);
        List<Slot> slots = query.getResultList();
        return slots.isEmpty() ? null : slots.get(0);
    }

    @Override
    public List<Slot> findByStationId(int stationId) {
        // JPQL query to find slots by charging station ID
        String jpql = "SELECT s FROM Slot s WHERE s.chargingStation.id = :stationId";
        TypedQuery<Slot> query = em.createQuery(jpql, Slot.class);
        query.setParameter("stationId", stationId); // Use the int value directly
        return query.getResultList();
    }
    

    
    
}
