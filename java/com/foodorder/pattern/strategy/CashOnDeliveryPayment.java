package com.foodorder.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
public class CashOnDeliveryPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {
        System.out.println("Cash on Delivery selected for ₹" + amount);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "CASH_ON_DELIVERY";
    }
}
