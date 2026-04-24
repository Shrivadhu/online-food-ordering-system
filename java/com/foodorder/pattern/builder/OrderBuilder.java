package com.foodorder.pattern.builder;

import com.foodorder.enums.OrderStatus;
import com.foodorder.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BUILDER PATTERN (Creational)
 * Constructs complex Order objects step-by-step.
 * Separates the construction of an Order from its representation,
 * allowing the same construction process to create different types of orders.
 */
@Component
public class OrderBuilder {

    private Customer customer;
    private Restaurant restaurant;
    private DeliveryPerson deliveryPerson;
    private String deliveryAddress;
    private double totalAmount;
    private List<CartItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.PLACED;
    private LocalDateTime orderDate = LocalDateTime.now();

    public OrderBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder withRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public OrderBuilder withDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
        return this;
    }

    public OrderBuilder withDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public OrderBuilder withTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public OrderBuilder withItems(List<CartItem> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public Order build() {
        if (customer == null) throw new IllegalStateException("Customer is required to build an Order");
        if (restaurant == null) throw new IllegalStateException("Restaurant is required to build an Order");
        if (deliveryAddress == null || deliveryAddress.isBlank())
            throw new IllegalStateException("Delivery address is required");

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryPerson(deliveryPerson);
        order.setDeliveryAddress(deliveryAddress);
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        order.setStatus(status);
        order.setOrderDate(orderDate);
        return order;
    }

    /** Reset builder state for reuse */
    public OrderBuilder reset() {
        this.customer = null;
        this.restaurant = null;
        this.deliveryPerson = null;
        this.deliveryAddress = null;
        this.totalAmount = 0;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.orderDate = LocalDateTime.now();
        return this;
    }
}
