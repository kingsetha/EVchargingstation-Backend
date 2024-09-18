package com.ani.home.serviceimpl;

import com.ani.home.dto.PaymentRequestDTO;
import com.ani.home.mapper.PaymentMapper;
import com.ani.home.model.Booking;
import com.ani.home.model.Payment;
import com.ani.home.repo.PaymentRepo;
import com.ani.home.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    

   
    public PaymentServiceImpl(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
        
    }

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getBooking() != null) {
            Booking booking = payment.getBooking();
            if (booking.getDeviceType() == null) {
                throw new IllegalArgumentException("Booking deviceType cannot be null");
            }
            
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
            
            if (paymentRequest == null || paymentRequest.getBooking() == null) {
                throw new IllegalArgumentException("Invalid payment request data.");
            }

            
            Payment payment = paymentMapper.toEntity(paymentRequest);

          
            boolean paymentSuccess = true; 
            
            if (paymentSuccess) {
                
                paymentRepo.save(payment);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
    }
}
