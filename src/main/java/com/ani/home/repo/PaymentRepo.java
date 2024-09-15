package com.ani.home.repo;

import com.ani.home.model.Payment;

import java.util.Optional;

public interface PaymentRepo {
    Payment save(Payment payment);
    Optional<Payment> findById(int paymentId);
    boolean existsById(int paymentId);
    void deleteById(int paymentId);
}
