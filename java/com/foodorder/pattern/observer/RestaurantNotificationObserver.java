package com.foodorder.pattern.observer;

import com.foodorder.model.Order;
import org.springframework.stereotype.Component;

@Component
public class RestaurantNotificationObserver implements OrderObserver {

    @Override
    public void update(Order order, String message) {
        System.out.println("[RESTAURANT NOTIFICATION] " + order.getRestaurant().getRestaurantName() + ": " + message);
    }
}
