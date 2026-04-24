package com.foodorder.pattern.observer;

import com.foodorder.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject in Observer Pattern.
 * Maintains a list of observers and notifies them on order status changes.
 */
@Component
public class OrderNotificationService {

    private final List<OrderObserver> observers = new ArrayList<>();

    public void registerObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Order order, String message) {
        for (OrderObserver observer : observers) {
            observer.update(order, message);
        }
    }

    public void notifyStatusChange(Order order) {
        String message = "Order #" + order.getOrderId() + " status changed to: " + order.getStatus();
        notifyObservers(order, message);
    }
}
