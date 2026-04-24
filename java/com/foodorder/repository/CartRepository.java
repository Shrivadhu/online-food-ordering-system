package com.foodorder.repository;

import com.foodorder.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.customer LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.foodItem f LEFT JOIN FETCH f.menu m LEFT JOIN FETCH m.restaurant WHERE c.customer.userId = :customerId")
    Optional<Cart> findByCustomerUserId(@Param("customerId") Long customerId);
}
