package com.foodorder.repository;

import com.foodorder.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.foodItems WHERE m.restaurant.userId = :restaurantId")
    List<Menu> findByRestaurantUserId(@Param("restaurantId") Long restaurantId);
}
