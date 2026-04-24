package com.foodorder.service;

import com.foodorder.enums.*;
import com.foodorder.model.*;
import com.foodorder.pattern.builder.OrderBuilder;
import com.foodorder.pattern.observer.*;
import com.foodorder.pattern.strategy.*;
import com.foodorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private DeliveryPersonRepository deliveryPersonRepository;
    @Autowired private CartService cartService;

    // Observer Pattern
    @Autowired private OrderNotificationService notificationService;
    @Autowired private CustomerNotificationObserver customerObserver;
    @Autowired private RestaurantNotificationObserver restaurantObserver;

    // Builder Pattern
    @Autowired private OrderBuilder orderBuilder;

    // Strategy Pattern
    @Autowired private CreditCardPayment creditCardPayment;
    @Autowired private UPIPayment upiPayment;
    @Autowired private CashOnDeliveryPayment cashOnDeliveryPayment;
    @Autowired private PaymentContext paymentContext;

    @Transactional
    public Order placeOrder(Long customerId, Long restaurantId, String paymentType, String deliveryAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Cart cart = cartService.getCart(customerId);
        if (cart.getItems().isEmpty()) throw new RuntimeException("Cart is empty");

        // BUILDER PATTERN — construct Order step-by-step, cleanly and safely
        Order order = orderBuilder.reset()
                .withCustomer(customer)
                .withRestaurant(restaurant)
                .withDeliveryAddress(deliveryAddress)
                .withTotalAmount(cart.calculateTotal())
                .withItems(new ArrayList<>(cart.getItems()))
                .withStatus(OrderStatus.PLACED)
                .build();

        Order savedOrder = orderRepository.save(order);

        // STRATEGY PATTERN - select payment strategy
        PaymentType pType = PaymentType.valueOf(paymentType);
        selectPaymentStrategy(pType);
        boolean paymentSuccess = paymentContext.executePayment(order.getTotalAmount());

        Payment payment = new Payment(order.getTotalAmount(), pType, savedOrder);
        payment.setPaymentStatus(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentRepository.save(payment);

        // OBSERVER PATTERN - notify observers
        notificationService.registerObserver(customerObserver);
        notificationService.registerObserver(restaurantObserver);
        notificationService.notifyStatusChange(savedOrder);

        // Clear cart after order
        cartService.clearCart(customerId);

        return savedOrder;
    }

    private void selectPaymentStrategy(PaymentType type) {
        switch (type) {
            case CREDIT_CARD -> paymentContext.setStrategy(creditCardPayment);
            case UPI -> paymentContext.setStrategy(upiPayment);
            case CASH_ON_DELIVERY -> paymentContext.setStrategy(cashOnDeliveryPayment);
        }
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        notificationService.notifyStatusChange(updated);
        return updated;
    }

    @Transactional
    public Order assignDeliveryPerson(Long orderId, Long deliveryPersonId) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        DeliveryPerson dp = deliveryPersonRepository.findById(deliveryPersonId)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
        order.setDeliveryPerson(dp);
        order.setStatus(OrderStatus.OUT_OF_DELIVERY);
        dp.setDeliveryStatus(DeliveryStatus.ASSIGNED);
        deliveryPersonRepository.save(dp);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerUserIdOrderByOrderDateDesc(customerId);
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantUserIdOrderByOrderDateDesc(restaurantId);
    }

    public List<Order> getOrdersByDeliveryPerson(Long dpId) {
        return orderRepository.findByDeliveryPersonUserIdOrderByOrderDateDesc(dpId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllWithDetails();
    }

    public Optional<Order> findById(Long orderId) {
        return orderRepository.findByIdWithDetails(orderId);
    }

    public long getTotalOrders() { return orderRepository.count(); }
}
