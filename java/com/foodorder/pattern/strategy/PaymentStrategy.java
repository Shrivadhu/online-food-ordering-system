package com.foodorder.pattern.strategy;

/**
 * STRATEGY PATTERN (Behavioral)
 * Defines a family of payment algorithms, encapsulates each one,
 * and makes them interchangeable. Allows payment method to vary independently.
 */
public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentType();
}
