package com.ani.home.service;

import com.ani.home.model.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(int paymentId);
    Payment updatePayment(Payment payment);
    void deletePayment(int paymentId);
}
