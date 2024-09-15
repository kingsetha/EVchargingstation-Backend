//package com.ani.home.repoimpl;
//
//import com.ani.home.model.Payment;
//import com.ani.home.repo.PaymentRepo;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//@Transactional
//public class PaymentRepoImpl implements PaymentRepo {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public Payment save(Payment payment) {
//        if (payment != null) {
//            return em.merge(payment); // Use merge for both insert and update
//        }
//        return null; // Returning null to indicate failure
//    }
//
//    @Override
//    public Optional<Payment> findById(int paymentId) {
//        Payment payment = em.find(Payment.class, paymentId);
//        return Optional.ofNullable(payment);
//    }
//
//    @Override
//    public boolean existsById(int paymentId) {
//        return em.find(Payment.class, paymentId) != null;
//    }
//
//    @Override
//    public void deleteById(int paymentId) {
//        Payment payment = em.find(Payment.class, paymentId);
//        if (payment != null) {
//            em.remove(payment);
//        }
//    }
//}


package com.ani.home.repoimpl;

import com.ani.home.model.Payment;
import com.ani.home.repo.PaymentRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public class PaymentRepoImpl implements PaymentRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Payment save(Payment payment) {
        if (payment.getPaymentId() == 0) {
            // Insert new payment
            em.persist(payment);
            return payment;
        } else {
            // Update existing payment
            if (em.find(Payment.class, payment.getPaymentId()) == null) {
                throw new RuntimeException("Payment not found");
            }
            return em.merge(payment);
        }
    }

    @Override
    public Optional<Payment> findById(int paymentId) {
        return Optional.ofNullable(em.find(Payment.class, paymentId));
    }

    @Override
    public boolean existsById(int paymentId) {
        return em.find(Payment.class, paymentId) != null;
    }

    @Override
    public void deleteById(int paymentId) {
        Payment payment = em.find(Payment.class, paymentId);
        if (payment != null) {
            em.remove(payment);
        } else {
            throw new RuntimeException("Payment not found");
        }
    }
}
