////package com.ani.home.serviceimpl;
////
////import com.ani.home.model.Payment;
////import com.ani.home.repo.PaymentRepo;
////import com.ani.home.service.PaymentService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
//
////@Service
////public class PaymentServiceImpl implements PaymentService {
////
////    private final PaymentRepo paymentRepository;
////
////    @Autowired
////    public PaymentServiceImpl(PaymentRepo paymentRepository) {
////        this.paymentRepository = paymentRepository;
////    }
////
////    @Override
////    public Payment createPayment(Payment payment) {
////        return paymentRepository.save(payment);
////    }
////
////    @Override
////    public Payment getPaymentById(int paymentId) {
////        return paymentRepository.findById(paymentId)
////                .orElseThrow(() -> new RuntimeException("Payment not found"));
////    }
////
////    @Override
////    public Payment updatePayment(Payment payment) {
////        if (!paymentRepository.existsById(payment.getPaymentId())) {
////            throw new RuntimeException("Payment not found");
////        }
////        return paymentRepository.save(payment);
////    }
////
////    @Override
////    public void deletePayment(int paymentId) {
////        if (!paymentRepository.existsById(paymentId)) {
////            throw new RuntimeException("Payment not found");
////        }
////        paymentRepository.deleteById(paymentId);
////    }
////}
////
////package com.ani.home.serviceimpl;
////
////import com.ani.home.model.Booking;
////import com.ani.home.model.Payment;
////import com.ani.home.repo.BookingRepo;
////import com.ani.home.repo.PaymentRepo;
////import com.ani.home.service.PaymentService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////
////@Service
////public class PaymentServiceImpl implements PaymentService {
////
////    private final PaymentRepo paymentRepo;
////    private final BookingRepo bookingRepo;
////
////    @Autowired
////    public PaymentServiceImpl(PaymentRepo paymentRepo, BookingRepo bookingRepo) {
////        this.paymentRepo = paymentRepo;
////        this.bookingRepo = bookingRepo;
////    }
////    @Override
////    public Payment createPayment(Payment payment) {
////        // Log Booking details to verify
////        if (payment.getBooking() != null) {
////            Booking booking = payment.getBooking();
////            System.out.println("Booking Details: " + booking);
////            // Validate if deviceType and other fields are not null
////            if (booking.getDeviceType() == null) {
////                throw new RuntimeException("Booking deviceType cannot be null");
////            }
////        }
////        return paymentRepo.save(payment);
////    }
////
////    @Override
////    public Payment getPaymentById(int paymentId) {
////        return paymentRepo.findById(paymentId)
////                .orElseThrow(() -> new RuntimeException("Payment not found"));
////    }
////
////    @Override
////    public Payment updatePayment(Payment payment) {
////        if (!paymentRepo.existsById(payment.getPaymentId())) {
////            throw new RuntimeException("Payment not found");
////        }
////        return paymentRepo.save(payment);
////    }
////
////    @Override
////    public void deletePayment(int paymentId) {
////        if (!paymentRepo.existsById(paymentId)) {
////            throw new RuntimeException("Payment not found");
////        }
////        paymentRepo.deleteById(paymentId);
////    }
////}
////
//
//
//package com.ani.home.serviceimpl;
//
//import com.ani.home.model.Booking;
//import com.ani.home.model.Payment;
//import com.ani.home.repo.BookingRepo;
//import com.ani.home.repo.PaymentRepo;
//import com.ani.home.service.PaymentService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
//
//    private final PaymentRepo paymentRepo;
//    private final BookingRepo bookingRepo;
//
//    @Autowired
//    public PaymentServiceImpl(PaymentRepo paymentRepo, BookingRepo bookingRepo) {
//        this.paymentRepo = paymentRepo;
//        this.bookingRepo = bookingRepo;
//    }
//
//    @Transactional
//    @Override
//    public Payment createPayment(Payment payment) {
//        // Log Booking details to verify
//        if (payment.getBooking() != null) {
//            Booking booking = payment.getBooking();
//            logger.info("Booking Details: {}", booking);
//
//            // Validate if deviceType and other fields are not null
//            if (booking.getDeviceType() == null) {
//                throw new IllegalArgumentException("Booking deviceType cannot be null");
//            }
//        } else {
//            throw new IllegalArgumentException("Payment must be associated with a booking");
//        }
//        return paymentRepo.save(payment);
//    }
//
//    @Override
//    public Payment getPaymentById(int paymentId) {
//        return paymentRepo.findById(paymentId)
//                .orElseThrow(() -> new RuntimeException("Payment with ID " + paymentId + " not found"));
//    }
//
//    @Transactional
//    @Override
//    public Payment updatePayment(Payment payment) {
//        if (!paymentRepo.existsById(payment.getPaymentId())) {
//            throw new RuntimeException("Payment with ID " + payment.getPaymentId() + " not found");
//        }
//        return paymentRepo.save(payment);
//    }
//
//    @Transactional
//    @Override
//    public void deletePayment(int paymentId) {
//        if (!paymentRepo.existsById(paymentId)) {
//            throw new RuntimeException("Payment with ID " + paymentId + " not found");
//        }
//        paymentRepo.deleteById(paymentId);
//    }
//}
//
package com.ani.home.serviceimpl;

import com.ani.home.dto.PaymentRequestDTO;
import com.ani.home.mapper.PaymentMapper;
import com.ani.home.model.Booking;
import com.ani.home.model.Payment;
import com.ani.home.repo.BookingRepo;
import com.ani.home.repo.PaymentRepo;
import com.ani.home.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final BookingRepo bookingRepo;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, BookingRepo bookingRepo) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getBooking() != null) {
            Booking booking = payment.getBooking();
            if (booking.getDeviceType() == null) {
                throw new IllegalArgumentException("Booking deviceType cannot be null");
            }
            // Additional checks can be added here
        }
        return paymentRepo.save(payment);
    }

    @Override
    public Payment getPaymentById(int paymentId) {
        return paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @Override
    public Payment updatePayment(Payment payment) {
        if (!paymentRepo.existsById(payment.getPaymentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        return paymentRepo.save(payment);
    }

    @Override
    public void deletePayment(int paymentId) {
        if (!paymentRepo.existsById(paymentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        paymentRepo.deleteById(paymentId);
    }

    @Autowired
    private PaymentMapper paymentMapper;

    public boolean processPayment(PaymentRequestDTO paymentRequest) {
        try {
            // Validate the payment request
            if (paymentRequest == null || paymentRequest.getBooking() == null) {
                throw new IllegalArgumentException("Invalid payment request data.");
            }

            // Convert PaymentRequestDTO to Payment entity
            Payment payment = paymentMapper.toEntity(paymentRequest);

            // Implement your payment processing logic here
            // For example, you might interact with a payment gateway API
            
            // Simulate payment processing success
            boolean paymentSuccess = true; // Replace with actual payment processing logic
            
            if (paymentSuccess) {
                // Save the payment to the database
                paymentRepo.save(payment);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Log the exception (consider using a logging framework)
            e.printStackTrace();
            return false;
        }
    }
}
