package com.foodorder.repository;

import com.foodorder.model.Order;
import com.foodorder.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.deliveryPerson WHERE o.customer.userId = :customerId ORDER BY o.orderDate DESC")
    List<Order> findByCustomerUserIdOrderByOrderDateDesc(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.deliveryPerson WHERE o.restaurant.userId = :restaurantId ORDER BY o.orderDate DESC")
    List<Order> findByRestaurantUserIdOrderByOrderDateDesc(@Param("restaurantId") Long restaurantId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.restaurant WHERE o.deliveryPerson.userId = :dpId ORDER BY o.orderDate DESC")
    List<Order> findByDeliveryPersonUserIdOrderByOrderDateDesc(@Param("dpId") Long dpId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.restaurant LEFT JOIN FETCH o.deliveryPerson ORDER BY o.orderDate DESC")
    List<Order> findAllWithDetails();

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.restaurant LEFT JOIN FETCH o.deliveryPerson WHERE o.orderId = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    List<Order> findByStatus(OrderStatus status);
    List<Order> findByRestaurantUserIdAndStatus(Long restaurantId, OrderStatus status);
}
