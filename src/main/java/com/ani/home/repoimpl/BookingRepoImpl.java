

package com.ani.home.repoimpl;

import com.ani.home.model.Booking;
import com.ani.home.model.StationAdmin;
import com.ani.home.repo.BookingRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BookingRepoImpl implements BookingRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Booking save(Booking booking) {
        if (booking != null) {
            if (booking.getBookingId() == 0) {
                em.persist(booking);
                return booking;
            } else {
                return em.merge(booking);
            }
        }
        return null;
    }

    @Override
    public Optional<Booking> findById(int bookingId) {
        return Optional.ofNullable(em.find(Booking.class, bookingId));
    }

    @Override
    public List<Booking> findAll() {
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    @Override
    public void deleteById(int bookingId) {
        Booking booking = em.find(Booking.class, bookingId);
        if (booking != null) {
            em.remove(booking);
        }
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.user.id = :userId", Booking.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }

//    @Override
//    public List<Booking> findBySlotChargingStation(StationAdmin station) {
//        // Check if the station is null
//        if (station == null) {
//            return List.of(); // Return an empty list if station is null
//        }
//
//        // Create a query to fetch bookings where the slot's charging station matches the given station
//        return em.createQuery("SELECT b FROM Booking b WHERE b.slot.chargingStation = :station", Booking.class)
//                 .setParameter("station", station)
//                 .getResultList();
//    }
    

    @Override
    public List<Booking> findBySlotChargingStationId(int stationId) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.slot.chargingStation.id = :stationId", Booking.class)
                 .setParameter("stationId", stationId)
                 .getResultList();
    }

}
