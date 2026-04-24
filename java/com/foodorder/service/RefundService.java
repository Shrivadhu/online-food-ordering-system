package com.foodorder.service;

import com.foodorder.enums.OrderStatus;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.model.Order;
import com.foodorder.model.Payment;
import com.foodorder.model.Refund;
import com.foodorder.repository.OrderRepository;
import com.foodorder.repository.PaymentRepository;
import com.foodorder.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RefundService {

    @Autowired private RefundRepository refundRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private OrderRepository orderRepository;

    @Transactional
    public Refund processRefund(Long orderId, String reason) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this order"));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Cannot refund — payment was not successful");
        }

        // Check if refund already exists
        Optional<Refund> existing = refundRepository.findByPaymentPaymentId(payment.getPaymentId());
        if (existing.isPresent()) {
            throw new RuntimeException("Refund already processed for this order");
        }

        Refund refund = new Refund();
        refund.setRefundAmount(payment.getAmount());
        refund.setReason(reason != null ? reason : "Refund requested");
        refund.setPayment(payment);

        // Mark payment as FAILED (refunded)
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        // Cancel the order
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return refundRepository.save(refund);
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAllByOrderByRefundDateDesc();
    }

    public Optional<Refund> getRefundByPaymentId(Long paymentId) {
        return refundRepository.findByPaymentPaymentId(paymentId);
    }
}
