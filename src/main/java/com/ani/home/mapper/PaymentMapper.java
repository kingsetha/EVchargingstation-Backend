package com.ani.home.mapper;

import com.ani.home.dto.PaymentRequestDTO;
import com.ani.home.model.Payment;
import com.ani.home.model.Payment.PaymentMethod;
import com.ani.home.model.Payment.PaymentStatus;
import com.ani.home.model.Booking;
import org.springframework.stereotype.Component;

@Component
//Assuming your enums are defined in different packages or need conversion
public class PaymentMapper {

 public Payment toEntity(PaymentRequestDTO dto) {
     if (dto == null) {
         throw new IllegalArgumentException("PaymentRequestDTO cannot be null");
     }

     Payment payment = new Payment();

     // Map Booking
     if (dto.getBooking() != null) {
         Booking booking = new Booking(); // Ensure this is the correct Booking entity
         booking.setBookingId(dto.getBooking().getBookingId());
         payment.setBooking(booking);
     } else {
         throw new IllegalArgumentException("Booking information is required");
     }

     // Map other fields
     payment.setAmount(dto.getAmount());

     // Handle enums with default values or conversions
     payment.setPaymentMethod(dto.getPaymentMethod() != null ? Payment.PaymentMethod.valueOf(dto.getPaymentMethod().name()) : Payment.PaymentMethod.CREDIT_CARD);
     payment.setPaymentStatus(dto.getPaymentStatus() != null ? Payment.PaymentStatus.valueOf(dto.getPaymentStatus().name()) : Payment.PaymentStatus.FAILED);

     payment.setPaymentTime(dto.getPaymentTime());
     payment.setSlotId(dto.getSlotId());

     return payment;
 }

 public PaymentRequestDTO toDTO(Payment payment) {
     if (payment == null) {
         throw new IllegalArgumentException("Payment entity cannot be null");
     }

     PaymentRequestDTO dto = new PaymentRequestDTO();

     // Map Booking
     if (payment.getBooking() != null) {
         PaymentRequestDTO.Booking booking = new PaymentRequestDTO.Booking();
         booking.setBookingId(payment.getBooking().getBookingId());
         dto.setBooking(booking);
     } else {
         throw new IllegalArgumentException("Booking information is missing in the Payment entity");
     }

     // Map other fields
     dto.setAmount(payment.getAmount());
     dto.setPaymentMethod(payment.getPaymentMethod() != null ? PaymentRequestDTO.PaymentMethod.valueOf(payment.getPaymentMethod().name()) : null);
     dto.setPaymentStatus(payment.getPaymentStatus() != null ? PaymentRequestDTO.PaymentStatus.valueOf(payment.getPaymentStatus().name()) : null);
     dto.setPaymentTime(payment.getPaymentTime());
     dto.setSlotId(payment.getSlotId());

     return dto;
 }
}
