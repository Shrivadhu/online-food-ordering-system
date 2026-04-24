package com.foodorder.repository;

import com.foodorder.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmail(String email);
    List<Restaurant> findByLocationContainingIgnoreCase(String location);
    List<Restaurant> findByRestaurantNameContainingIgnoreCase(String name);
}
