package com.foodorder.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        // Simulate credit card payment processing
        System.out.println("Processing Credit Card payment of ₹" + amount);
        return true; // Simulate success
    }

    @Override
    public String getPaymentType() {
        return "CREDIT_CARD";
    }
}
