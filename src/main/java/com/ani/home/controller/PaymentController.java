


package com.ani.home.controller;

import com.ani.home.model.Booking;
import com.ani.home.model.Payment;
import com.ani.home.model.Slot;

import com.ani.home.model.User;

import com.ani.home.service.BookingService;
import com.ani.home.service.EmailService;
import com.ani.home.service.PaymentService;
import com.ani.home.service.SlotService;


import jakarta.mail.MessagingException;

import java.util.Optional;

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
    private EmailService emailService;
   
   
    public PaymentController(PaymentService paymentService, BookingService bookingService, SlotService slotService,EmailService emailservice) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
        this.slotService = slotService;
        this.emailService=emailservice;
        
        
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        
        System.out.println("Received payment request: " + payment);

        
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
            
            Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
            if (!optionalBooking.isPresent()) {
                System.err.println("Error processing payment: Booking not found for bookingId: " + bookingId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Booking fetchedBooking = optionalBooking.get();
            payment.setBooking(fetchedBooking);

            
            Payment createdPayment = paymentService.createPayment(payment);
            System.out.println("Saved Payment: " + createdPayment);

           
            fetchedBooking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingService.updateBooking(fetchedBooking);

            
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
                	    mapImageLink 
                	);

                System.out.println("Payment confirmation email sent.");
            } catch (MessagingException e) {
                System.err.println("Error sending payment confirmation email: " + e.getMessage());
            }
        } else {
            System.err.println("Error processing payment: User email is not available.");
        }
    }

   

    
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




    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable("id") int id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            if (payment == null) {
                
                System.err.println("Payment not found with ID: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
           
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
