


package com.ani.home.controller;

import com.ani.home.model.Booking;
import com.ani.home.model.Payment;
import com.ani.home.model.Slot;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.User;
import com.ani.home.model.Booking.DeviceType;
import com.ani.home.model.Location;
import com.ani.home.model.LocationData;
import com.ani.home.model.MapUrlResponse;
import com.ani.home.service.BookingService;
import com.ani.home.service.EmailService;
import com.ani.home.service.PaymentService;
import com.ani.home.service.SlotService;
import com.ani.home.service.StationAdminService;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;
    private final BookingService bookingService;
    private final SlotService slotService;
    private final StationAdminService stationAdminService;
    @Autowired
    private EmailService emailService;// Add SlotService
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService, SlotService slotService,StationAdminService stationAdminService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
        this.slotService = slotService;
        this.stationAdminService=stationAdminService;
        
    }

//    @PostMapping
//    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
//        // Log incoming payment request
//        System.out.println("Received payment request: " + payment);
//
//        // Retrieve the booking associated with the payment
//        Booking booking = payment.getBooking();
//        if (booking == null) {
//            System.err.println("Error processing payment: Missing booking information.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Retrieve and validate the bookingId from the booking object
//        int bookingId = booking.getBookingId();
//        if (bookingId <= 0) {
//            System.err.println("Error processing payment: Missing or invalid bookingId.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Validate other required fields
//        if (payment.getAmount() == null || payment.getPaymentStatus() == null || payment.getSlotId() == null) {
//            System.err.println("Error processing payment: Missing required fields.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            // Log the bookingId being processed
//            System.out.println("Processing payment for bookingId: " + bookingId);
//
//            // Fetch the booking details using bookingId
//            Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
//            if (!optionalBooking.isPresent()) {
//                System.err.println("Error processing payment: Booking not found for bookingId: " + bookingId);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Booking fetchedBooking = optionalBooking.get();
//            // Update the booking with payment details
//            payment.setBooking(fetchedBooking);
//
//            // Create the payment record
//            Payment createdPayment = paymentService.createPayment(payment);
//
//            // Update the booking status to CONFIRMED
//            fetchedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
//            bookingService.updateBooking(fetchedBooking);
//
//            // Retrieve the slot using slotId from payment
//            int slotId = payment.getSlotId(); // slotId is now an Integer
//            Optional<Slot> optionalSlot = slotService.getSlotById(slotId);
//            if (!optionalSlot.isPresent()) {
//                System.err.println("Error processing payment: Slot not found for slotId: " + slotId);
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Slot slot = optionalSlot.get();
//            // Map DeviceType to ChargingType
//            Slot.ChargingType chargingType = mapDeviceTypeToChargingType(fetchedBooking.getDeviceType());
//
//            if (chargingType != null) {
//                try {
//                    slot.decreaseCount(chargingType); // Decrease slot count based on chargingType
//                    slotService.save(slot); // Save the updated slot
//                } catch (Exception e) {
//                    System.err.println("Error decreasing slot count: " + e.getMessage());
//                    // Revert booking status or handle the error as needed
//                    fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                    bookingService.updateBooking(fetchedBooking);
//                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            } else {
//                System.err.println("Error processing payment: ChargingType is null.");
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Return the created payment with a 201 Created status
//            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Log the exception
//            System.err.println("Error processing payment: " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping
//    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
//        // Log incoming payment request
//        System.out.println("Received payment request: " + payment);
//
//        // Retrieve the booking associated with the payment
//        Booking booking = payment.getBooking();
//        if (booking == null) {
//            System.err.println("Error processing payment: Missing booking information.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Retrieve and validate the bookingId from the booking object
//        int bookingId = booking.getBookingId();
//        if (bookingId <= 0) {
//            System.err.println("Error processing payment: Missing or invalid bookingId.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Validate other required fields
//        if (payment.getAmount() == null || payment.getPaymentStatus() == null || payment.getSlotId() == null) {
//            System.err.println("Error processing payment: Missing required fields.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            // Log the bookingId being processed
//            System.out.println("Processing payment for bookingId: " + bookingId);
//
//            // Fetch the booking details using bookingId
//            Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
//            if (!optionalBooking.isPresent()) {
//                System.err.println("Error processing payment: Booking not found for bookingId: " + bookingId);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Booking fetchedBooking = optionalBooking.get();
//            // Update the booking with payment details
//            payment.setBooking(fetchedBooking);
//
//            // Create the payment record
//            Payment createdPayment = paymentService.createPayment(payment);
//
//            // Update the booking status to CONFIRMED
//            fetchedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
//            bookingService.updateBooking(fetchedBooking);
//
//            // Retrieve the slot using slotId from payment
//            int slotId = payment.getSlotId(); // slotId is now an Integer
//            Optional<Slot> optionalSlot = slotService.getSlotById(slotId);
//            if (!optionalSlot.isPresent()) {
//                System.err.println("Error processing payment: Slot not found for slotId: " + slotId);
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Slot slot = optionalSlot.get();
//            // Map DeviceType to ChargingType
//            Slot.ChargingType chargingType = mapDeviceTypeToChargingType(fetchedBooking.getDeviceType());
//
//            if (chargingType != null) {
//                try {
//                    slot.decreaseCount(chargingType); // Decrease slot count based on chargingType
//                    slotService.save(slot); // Save the updated slot
//                } catch (Exception e) {
//                    System.err.println("Error decreasing slot count: " + e.getMessage());
//                    // Revert booking status or handle the error as needed
//                    fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                    bookingService.updateBooking(fetchedBooking);
//                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            } else {
//                System.err.println("Error processing payment: ChargingType is null.");
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Retrieve the user associated with the booking
//            User user = fetchedBooking.getUser(); // Assuming `getUser` returns the User associated with the booking
//            String userEmail = user != null ? user.getEmail() : null;
//
//            // Send payment confirmation email with map image
//            if (userEmail != null) {
//                try {
//                    // Generate map image URL or byte array using StationAdmin's coordinates
//                    byte[] mapImage = generateMapImageAsByteArray(slot.getChargingStation().getLatitude(), slot.getChargingStation().getLongitude());
//
//                    emailService.sendEmailWithMap(
//                        userEmail,
//                        "Payment Confirmation",
//                        String.format(
//                            "Dear %s,<br/><br/>Thank you for your payment.<br/><br/>" +
//                            "Booking ID: %s<br/>" +
//                            "Slot ID: %s<br/>" +
//                            "Amount Paid (with tax): $%.2f<br/>" +
//                            "Device Type: %s<br/>" +
//                            "Payment Method: %s<br/><br/>" +
//                            "Best regards,<br/>Your Company",
//                            user.getFullName(),
//                            bookingId,
//                            slotId,
//                            payment.getAmount(),
//                            fetchedBooking.getDeviceType(),
//                            payment.getPaymentMethod()
//                        ),
//                        mapImage
//                    );
//                    System.out.println("Payment confirmation email sent.");
//                } catch (MessagingException e) {
//                    System.err.println("Error sending payment confirmation email: " + e.getMessage());
//                    // Optionally, handle email sending error here (e.g., retry logic or notify admin)
//                }
//            } else {
//                System.err.println("Error processing payment: User email is not available.");
//            }
//
//            // Return the created payment with a 201 Created status
//            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Log the exception
//            System.err.println("Error processing payment: " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    private byte[] generateMapImageAsByteArray(BigDecimal lat, BigDecimal lon) {
//        // Implement map generation logic using the StationAdmin's latitude and longitude
//        // Example: Use a static map API or screenshot a Leaflet map
//        return new byte[0]; // Placeholder
//    }
//    @PostMapping
//    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
//        // Log incoming payment request
//        System.out.println("Received payment request: " + payment);
//
//        // Retrieve the booking associated with the payment
//        Booking booking = payment.getBooking();
//        if (booking == null) {
//            System.err.println("Error processing payment: Missing booking information.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Log map image from request
//        String mapImage = payment.getMapImage();
//        System.out.println("Map Image from request: " + mapImage);
//
//        // Retrieve and validate the bookingId from the booking object
//        int bookingId = booking.getBookingId();
//        if (bookingId <= 0) {
//            System.err.println("Error processing payment: Missing or invalid bookingId.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Validate other required fields
//        if (payment.getAmount() == null || payment.getPaymentStatus() == null || payment.getSlotId() == null) {
//            System.err.println("Error processing payment: Missing required fields.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            // Log the bookingId being processed
//            System.out.println("Processing payment for bookingId: " + bookingId);
//
//            // Fetch the booking details using bookingId
//            Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
//            if (!optionalBooking.isPresent()) {
//                System.err.println("Error processing payment: Booking not found for bookingId: " + bookingId);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Booking fetchedBooking = optionalBooking.get();
//            // Update the booking with payment details
//            payment.setBooking(fetchedBooking);
//
//            // Log map image to be saved
//            System.out.println("Map Image to be saved: " + payment.getMapImage());
//
//            // Create the payment record
//            Payment createdPayment = paymentService.createPayment(payment);
//
//            // Log the saved payment details
//            Payment savedPayment = paymentService.getPaymentById(createdPayment.getPaymentId());
//            System.out.println("Saved Payment: " + savedPayment);
//
//            // Update the booking status to CONFIRMED
//            fetchedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
//            bookingService.updateBooking(fetchedBooking);
//
//            // Retrieve the slot using slotId from payment
//            int slotId = payment.getSlotId();
//            Optional<Slot> optionalSlot = slotService.getSlotById(slotId);
//            if (!optionalSlot.isPresent()) {
//                System.err.println("Error processing payment: Slot not found for slotId: " + slotId);
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            Slot slot = optionalSlot.get();
//            // Map DeviceType to ChargingType
//            Slot.ChargingType chargingType = mapDeviceTypeToChargingType(fetchedBooking.getDeviceType());
//
//            if (chargingType != null) {
//                try {
//                    slot.decreaseCount(chargingType); // Decrease slot count based on chargingType
//                    slotService.save(slot); // Save the updated slot
//                } catch (Exception e) {
//                    System.err.println("Error decreasing slot count: " + e.getMessage());
//                    // Revert booking status or handle the error as needed
//                    fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                    bookingService.updateBooking(fetchedBooking);
//                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            } else {
//                System.err.println("Error processing payment: ChargingType is null.");
//                // Revert booking status or handle the error as needed
//                fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
//                bookingService.updateBooking(fetchedBooking);
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Retrieve the user associated with the booking
//            User user = fetchedBooking.getUser(); // Assuming `getUser` returns the User associated with the booking
//            String userEmail = user != null ? user.getEmail() : null;
//
//            // Send payment confirmation email with map image
//            if (userEmail != null) {
//                try {
//                    // Generate map image URL or byte array using StationAdmin's coordinates
//                    byte[] mapImageBytes = mapImage != null ? decodeBase64ToByteArray(mapImage) : new byte[0];
//
//                    emailService.sendEmailWithMap(
//                        userEmail,
//                        "Payment Confirmation",
//                        String.format(
//                            "Dear %s,<br/><br/>Thank you for your payment.<br/><br/>" +
//                            "Booking ID: %s<br/>" +
//                            "Slot ID: %s<br/>" +
//                            "Amount Paid (with tax): $%.2f<br/>" +
//                            "Device Type: %s<br/>" +
//                            "Payment Method: %s<br/><br/>" +
//                            "Best regards,<br/>Your Company",
//                            user.getFullName(),
//                            bookingId,
//                            slotId,
//                            payment.getAmount(),
//                            fetchedBooking.getDeviceType(),
//                            payment.getPaymentMethod()
//                        ),
//                        mapImageBytes
//                    );
//                    System.out.println("Payment confirmation email sent.");
//                } catch (MessagingException e) {
//                    System.err.println("Error sending payment confirmation email: " + e.getMessage());
//                    // Optionally, handle email sending error here (e.g., retry logic or notify admin)
//                }
//            } else {
//                System.err.println("Error processing payment: User email is not available.");
//            }
//
//            // Return the created payment with a 201 Created status
//            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Log the exception
//            System.err.println("Error processing payment: " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private byte[] decodeBase64ToByteArray(String base64Image) {
//        try {
//            // Remove any prefix such as "data:image/png;base64,"
//            if (base64Image.startsWith("data:image")) {
//                base64Image = base64Image.split(",")[1];
//            }
//            return java.util.Base64.getDecoder().decode(base64Image);
//        } catch (IllegalArgumentException e) {
//            System.err.println("Failed to decode Base64 image: " + e.getMessage());
//            return new byte[0];
//        }
//    }


    
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        // Log incoming payment request
        System.out.println("Received payment request: " + payment);

        // Validate the payment object
        if (payment == null || payment.getBooking() == null || payment.getAmount() == null 
            || payment.getPaymentStatus() == null || payment.getSlotId() == null) {
            System.err.println("Error processing payment: Missing required fields.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Booking booking = payment.getBooking();
        int bookingId = booking.getBookingId();

        if (bookingId <= 0) {
            System.err.println("Error processing payment: Missing or invalid bookingId.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // Fetch the booking details using bookingId
            Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
            if (!optionalBooking.isPresent()) {
                System.err.println("Error processing payment: Booking not found for bookingId: " + bookingId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Booking fetchedBooking = optionalBooking.get();
            payment.setBooking(fetchedBooking);

            // Create the payment record
            Payment createdPayment = paymentService.createPayment(payment);
            System.out.println("Saved Payment: " + createdPayment);

            // Update the booking status to CONFIRMED
            fetchedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingService.updateBooking(fetchedBooking);

            // Retrieve and update the slot
            int slotId = payment.getSlotId();
            Optional<Slot> optionalSlot = slotService.getSlotById(slotId);
            if (!optionalSlot.isPresent()) {
                handleSlotNotFound(fetchedBooking);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Slot slot = optionalSlot.get();
            Slot.ChargingType chargingType = mapDeviceTypeToChargingType(fetchedBooking.getDeviceType());

            if (chargingType != null) {
                try {
                    slot.decreaseCount(chargingType);
                    slotService.save(slot);
                } catch (Exception e) {
                    System.err.println("Error decreasing slot count: " + e.getMessage());
                    handleSlotUpdateError(fetchedBooking);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                System.err.println("Error processing payment: ChargingType is null.");
                handleSlotUpdateError(fetchedBooking);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Send payment confirmation email with map location URL
            sendPaymentConfirmationEmail(fetchedBooking, payment);

            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    


    private void handleSlotNotFound(Booking fetchedBooking) {
        fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
        bookingService.updateBooking(fetchedBooking);
    }

    private void handleSlotUpdateError(Booking fetchedBooking) {
        fetchedBooking.setStatus(Booking.BookingStatus.PENDING);
        bookingService.updateBooking(fetchedBooking);
    }

    private void sendPaymentConfirmationEmail(Booking fetchedBooking, Payment payment) {
        User user = fetchedBooking.getUser();
        if (user != null && user.getEmail() != null) {
            try {
                String mapLocationUrl = payment.getMapLocationUrl();
                String mapImageLink = mapLocationUrl != null ? mapLocationUrl : "Map location not available";

                emailService.sendEmailWithMap(
                	    user.getEmail(),
                	    "Payment Confirmation",
                	    String.format(
                	        "Dear %s,<br/><br/>Thank you for your payment.<br/><br/>" +
                	        "Booking ID: %s<br/>" +
                	        "Slot ID: %s<br/>" +
                	        "Amount Paid (with tax): $%.2f<br/>" +
                	        "Device Type: %s<br/>" +
                	        "Payment Method: %s<br/><br/>" +
                	        "Best regards,<br/>Your Company",
                	        user.getFullName(),
                	        payment.getBooking().getBookingId(),
                	        payment.getSlotId(),
                	        payment.getAmount(),
                	        fetchedBooking.getDeviceType(),
                	        payment.getPaymentMethod()
                	    ),
                	    mapImageLink // URL to the map location
                	);

                System.out.println("Payment confirmation email sent.");
            } catch (MessagingException e) {
                System.err.println("Error sending payment confirmation email: " + e.getMessage());
            }
        } else {
            System.err.println("Error processing payment: User email is not available.");
        }
    }

    private byte[] decodeBase64ToByteArray(String base64Image) {
        try {
            // Remove any prefix such as "data:image/png;base64,"
            if (base64Image.startsWith("data:image")) {
                base64Image = base64Image.split(",")[1];
            }
            return java.util.Base64.getDecoder().decode(base64Image);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to decode Base64 image: " + e.getMessage());
            return new byte[0];
        }
    }

    

    // Utility method to map DeviceType to ChargingType
    private Slot.ChargingType mapDeviceTypeToChargingType(Booking.DeviceType deviceType) {
        if (deviceType == null) {
            System.err.println("Error: DeviceType is null.");
            return null;
        }

        switch (deviceType) {
            case LEVEL_1:
                return Slot.ChargingType.LEVEL_1;
            case LEVEL_2:
                return Slot.ChargingType.LEVEL_2;
            case DC_FAST_CHARGING:
                return Slot.ChargingType.DC_FAST_CHARGING;
            default:
                System.err.println("Error: Unexpected DeviceType: " + deviceType);
                return null;
        }
    }



//    @GetMapping("/{id}")
//    public ResponseEntity<Payment> getPayment(@PathVariable("id") int id) {
//        try {
//            Payment payment = paymentService.getPaymentById(id);
//            return new ResponseEntity<>(payment, HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(null, e.getStatusCode());
//        }
//    }
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable("id") int id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            if (payment == null) {
                // Log and return 404 if payment is not found
                System.err.println("Payment not found with ID: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception and return 500 for internal server error
            System.err.println("Error retrieving payment with ID: " + id + ". Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
        try {
            Payment updatedPayment = paymentService.updatePayment(payment);
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(null, e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") int id) {
        try {
            paymentService.deletePayment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
