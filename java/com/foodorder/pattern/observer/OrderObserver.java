package com.foodorder.pattern.observer;

import com.foodorder.model.Order;

/**
 * OBSERVER PATTERN (Behavioral)
 * Defines a one-to-many dependency so when an Order's status changes,
 * all registered observers (Customer, Restaurant, DeliveryPerson) are notified automatically.
 */
public interface OrderObserver {
    void update(Order order, String message);
}
