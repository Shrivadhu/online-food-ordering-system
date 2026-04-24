package com.foodorder.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
public class UPIPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        System.out.println("Processing UPI payment of ₹" + amount);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "UPI";
    }
}
