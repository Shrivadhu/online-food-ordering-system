package com.foodorder.model;

import com.foodorder.enums.PaymentStatus;
import com.foodorder.enums.PaymentType;
import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Payment() {
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public Payment(double amount, PaymentType paymentType, Order order) {
        this.amount = amount;
        this.paymentType = paymentType;
        this.order = order;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public void processPayment() {
        this.paymentStatus = PaymentStatus.SUCCESS;
    }

    public void refundPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
