package com.foodorder.service;

import com.foodorder.enums.PaymentStatus;
import com.foodorder.model.Payment;
import com.foodorder.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderOrderId(orderId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }
}
