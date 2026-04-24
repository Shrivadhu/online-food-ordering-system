package com.foodorder.service;

import com.foodorder.model.*;
import com.foodorder.pattern.factory.FoodItemFactory;
import com.foodorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired private MenuRepository menuRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private FoodItemFactory foodItemFactory;

    @Transactional
    public Menu createMenu(Long restaurantId, String menuName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Menu menu = new Menu(menuName, restaurant);
        return menuRepository.save(menu);
    }

    @Transactional
    public FoodItem addFoodItem(Long menuId, String type, String name, double price, String description) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        // FACTORY PATTERN used here
        FoodItem item = foodItemFactory.createFoodItem(type, name, price, description);
        item.setMenu(menu);
        return foodItemRepository.save(item);
    }

    @Transactional
    public FoodItem updateFoodItem(Long foodId, String name, double price, String description, boolean available) {
        FoodItem item = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        item.setAvailable(available);
        return foodItemRepository.save(item);
    }

    @Transactional
    public void deleteFoodItem(Long foodId) {
        foodItemRepository.deleteById(foodId);
    }

    public List<Menu> getMenusByRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantUserId(restaurantId);
    }

    public List<FoodItem> getFoodItemsByMenu(Long menuId) {
        return foodItemRepository.findByMenuMenuId(menuId);
    }

    public List<FoodItem> getFoodItemsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByMenuRestaurantUserId(restaurantId);
    }

    public List<FoodItem> searchFoodItems(String query) {
        return foodItemRepository.findByNameContainingIgnoreCaseAndAvailableTrue(query);
    }

    public Optional<FoodItem> findFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }

    public Optional<Menu> findMenuById(Long id) {
        return menuRepository.findById(id);
    }
}
