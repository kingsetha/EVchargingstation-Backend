

package com.ani.home.serviceimpl;

import com.ani.home.model.Booking;

import com.ani.home.repo.BookingRepo;
import com.ani.home.service.BookingService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());

    private final BookingRepo bookingRepo;

 
    public BookingServiceImpl(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Booking saveBooking(Booking booking) {
        validateBooking(booking);
        logger.info("Saving booking with ID: " + booking.getBookingId());
        return bookingRepo.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(int bookingId) {
        logger.info("Fetching booking with ID: " + bookingId);
        return bookingRepo.findById(bookingId);
    }

    @Override
    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepo.findAll();
    }

    @Override
    public void deleteBooking(int bookingId) {
        bookingRepo.deleteById(bookingId);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        validateBooking(booking);
        logger.info("Updating booking with ID: " + booking.getBookingId());
        return bookingRepo.save(booking);
    }

    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (booking.getDeviceType() == null) {
            throw new IllegalArgumentException("Booking deviceType cannot be null");
        }
        
    }

	@Override
	public List<Booking> getBookingsByUser(int userId) {
        return bookingRepo.findByUserId(userId);
    }

//	@Override
//	public List<Booking> getBookingsByStation(StationAdmin station) {
//	    logger.info("Fetching bookings for station with ID: " + station.getId());
//	    return bookingRepo.findBySlotChargingStation(station);
//	}
	@Override
	public List<Booking> getBookingsByStationId(int stationId) {
        return bookingRepo.findBySlotChargingStationId(stationId);
    }
}

