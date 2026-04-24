package com.foodorder.pattern.observer;

import com.foodorder.model.Order;
import org.springframework.stereotype.Component;

@Component
public class CustomerNotificationObserver implements OrderObserver {

    @Override
    public void update(Order order, String message) {
        // In a real app, send push notification / email to customer
        System.out.println("[CUSTOMER NOTIFICATION] " + order.getCustomer().getName() + ": " + message);
    }
}
