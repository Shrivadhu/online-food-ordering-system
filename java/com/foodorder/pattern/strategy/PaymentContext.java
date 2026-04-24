package com.foodorder.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
public class PaymentContext {

    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean executePayment(double amount) {
        if (strategy == null) throw new IllegalStateException("Payment strategy not set");
        return strategy.pay(amount);
    }
}
