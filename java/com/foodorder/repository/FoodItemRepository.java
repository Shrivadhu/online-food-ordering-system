package com.foodorder.repository;

import com.foodorder.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @Query("SELECT f FROM FoodItem f JOIN FETCH f.menu WHERE f.menu.menuId = :menuId")
    List<FoodItem> findByMenuMenuId(@Param("menuId") Long menuId);

    List<FoodItem> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    List<FoodItem> findByCategoryAndAvailableTrue(String category);

    @Query("SELECT f FROM FoodItem f JOIN FETCH f.menu m JOIN FETCH m.restaurant WHERE m.restaurant.userId = :restaurantId")
    List<FoodItem> findByMenuRestaurantUserId(@Param("restaurantId") Long restaurantId);
}
