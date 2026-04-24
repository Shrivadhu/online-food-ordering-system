package com.foodorder.service;

import com.foodorder.model.FoodItem;
import com.foodorder.model.Menu;
import com.foodorder.pattern.factory.FoodItemFactory;
import com.foodorder.pattern.prototype.FoodItemPrototype;
import com.foodorder.repository.FoodItemRepository;
import com.foodorder.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FoodFactory Service — orchestrates both FACTORY and PROTOTYPE patterns.
 *
 * - Factory Pattern  : create brand-new food items by type.
 * - Prototype Pattern: clone an existing item as a template for rapid variation.
 */
@Service
public class FoodFactoryService {

    @Autowired private FoodItemFactory foodItemFactory;     // FACTORY PATTERN
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private MenuRepository menuRepository;

    /**
     * FACTORY PATTERN: Create a new food item from scratch using type.
     */
    @Transactional
    public FoodItem createFoodItem(Long menuId, String type, String name,
                                   double price, String description) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        FoodItem item = foodItemFactory.createFoodItem(type, name, price, description);
        item.setMenu(menu);
        return foodItemRepository.save(item);
    }

    /**
     * PROTOTYPE PATTERN: Clone an existing food item as a new variant.
     * Copies all fields and saves as a new entity with a different name.
     */
    @Transactional
    public FoodItem cloneFoodItem(Long sourceFoodId, String newName, double newPrice) {
        FoodItem source = foodItemRepository.findById(sourceFoodId)
                .orElseThrow(() -> new RuntimeException("Source food item not found"));

        // Build prototype from existing item
        FoodItemPrototype prototype = new FoodItemPrototype(
                source.getName(),
                source.getPrice(),
                source.getCategory(),
                source.getDescription(),
                source.getImageUrl()
        );

        // Clone it
        FoodItemPrototype cloned = prototype.cloneObject();
        cloned.setName(newName);
        cloned.setPrice(newPrice);

        // Persist the cloned item under the same menu
        FoodItem newItem = new FoodItem();
        newItem.setName(cloned.getName());
        newItem.setPrice(cloned.getPrice());
        newItem.setCategory(cloned.getCategory());
        newItem.setDescription(cloned.getDescription());
        newItem.setImageUrl(cloned.getImageUrl());
        newItem.setAvailable(true);
        newItem.setMenu(source.getMenu());

        return foodItemRepository.save(newItem);
    }

    @Transactional
    public FoodItem updateFoodItem(Long foodId, String name, double price,
                                   String description, boolean available) {
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

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public List<FoodItem> getFoodItemsByMenu(Long menuId) {
        return foodItemRepository.findByMenuMenuId(menuId);
    }

    public List<FoodItem> getFoodItemsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByMenuRestaurantUserId(restaurantId);
    }

    public List<FoodItem> searchByCategory(String category) {
        return foodItemRepository.findByCategoryAndAvailableTrue(category);
    }
}
