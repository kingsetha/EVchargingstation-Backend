package com.ani.home.service;

import com.ani.home.model.Booking;


import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking saveBooking(Booking booking);
    Optional<Booking> getBookingById(int bookingId);
    List<Booking> getAllBookings();
    void deleteBooking(int bookingId);
    Booking updateBooking(Booking booking);
	List<Booking> getBookingsByUser(int userId);
//	List<Booking> getBookingsByStation(StationAdmin station);
	List<Booking> getBookingsByStationId(int stationId);
}
