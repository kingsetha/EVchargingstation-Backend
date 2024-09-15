

package com.ani.home.controller;

import com.ani.home.model.Booking;
import com.ani.home.model.StationAdmin;
import com.ani.home.service.BookingService;
import com.ani.home.service.StationAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final StationAdminService stationService;

    @Autowired
    public BookingController(BookingService bookingService,StationAdminService stationService) {
        this.bookingService = bookingService;
        this.stationService=stationService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            // Validate user and slot existence
            if (booking.getUser() == null || booking.getSlot() == null) {
                return ResponseEntity.badRequest().body("User or Slot is missing.");
            }
            // Save the booking
            Booking savedBooking = bookingService.saveBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } catch (Exception e) {
            // Log the error
            // In a real application, use a logger instead of printStackTrace
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the booking.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable int id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        Optional<Booking> existingBooking = bookingService.getBookingById(id);
        if (existingBooking.isPresent()) {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @PutMapping
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking) {
        if (booking.getBookingId() == 0) {
            return ResponseEntity.badRequest().body(null); // If no ID is provided, return bad request
        }
        Booking updatedBooking = bookingService.updateBooking(booking);
        return ResponseEntity.ok(updatedBooking);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable int userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUser(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    @GetMapping("/byStation")
//    public ResponseEntity<List<Booking>> getBookingsByStation(@RequestParam("stationId") int stationId) {
//        try {
//            StationAdmin station = stationService.getStationById(stationId);
//            if (station == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            List<Booking> bookings = bookingService.getBookingsByStation(station);
//            return ResponseEntity.ok(bookings);
//        } catch (Exception e) {
//            logger.error("Error occurred while fetching bookings by station: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @GetMapping("/byStation")
    public ResponseEntity<List<Booking>> getBookingsByStation(@RequestParam("stationId") int stationId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByStationId(stationId);
            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            logger.error("Error occurred while fetching bookings by station: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
