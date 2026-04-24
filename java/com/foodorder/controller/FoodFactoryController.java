package com.foodorder.controller;

import com.foodorder.model.FoodItem;
import com.foodorder.model.Menu;
import com.foodorder.model.Restaurant;
import com.foodorder.service.FoodFactoryService;
import com.foodorder.service.MenuService;
import com.foodorder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * FoodFactory Portal Controller
 * Provides a dedicated portal for creating and managing food items
 * using Factory (new item) and Prototype (clone item) design patterns.
 */
@Controller
@RequestMapping("/restaurant/factory")
public class FoodFactoryController {

    @Autowired private FoodFactoryService foodFactoryService;
    @Autowired private MenuService menuService;
    @Autowired private UserService userService;

    private Restaurant getRestaurant(Authentication auth) {
        return userService.findRestaurantByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    /**
     * FoodFactory Portal Dashboard
     */
    @GetMapping
    public String portal(Model model, Authentication auth) {
        Restaurant restaurant = getRestaurant(auth);
        List<Menu> menus = menuService.getMenusByRestaurant(restaurant.getUserId());
        List<FoodItem> allItems = foodFactoryService.getFoodItemsByRestaurant(restaurant.getUserId());

        long vegCount = allItems.stream().filter(i -> "Vegetarian".equals(i.getCategory())).count();
        long nonVegCount = allItems.stream().filter(i -> "Non-Vegetarian".equals(i.getCategory())).count();
        long beverageCount = allItems.stream().filter(i -> "Beverage".equals(i.getCategory())).count();
        long dessertCount = allItems.stream().filter(i -> "Dessert".equals(i.getCategory())).count();

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menus", menus);
        model.addAttribute("allItems", allItems);
        model.addAttribute("vegCount", vegCount);
        model.addAttribute("nonVegCount", nonVegCount);
        model.addAttribute("beverageCount", beverageCount);
        model.addAttribute("dessertCount", dessertCount);
        return "restaurant/factory";
    }

    /**
     * FACTORY PATTERN — Create a new food item from type
     */
    @PostMapping("/create")
    public String createItem(@RequestParam Long menuId,
                             @RequestParam String type,
                             @RequestParam String name,
                             @RequestParam double price,
                             @RequestParam String description,
                             Authentication auth,
                             RedirectAttributes ra) {
        try {
            foodFactoryService.createFoodItem(menuId, type, name, price, description);
            ra.addFlashAttribute("success", "✅ Factory created: '" + name + "' as " + type);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Failed to create item: " + e.getMessage());
        }
        return "redirect:/restaurant/factory";
    }

    /**
     * PROTOTYPE PATTERN — Clone an existing food item
     */
    @PostMapping("/clone/{foodId}")
    public String cloneItem(@PathVariable Long foodId,
                            @RequestParam String newName,
                            @RequestParam double newPrice,
                            RedirectAttributes ra) {
        try {
            FoodItem cloned = foodFactoryService.cloneFoodItem(foodId, newName, newPrice);
            ra.addFlashAttribute("success", "🔁 Prototype cloned: '" + cloned.getName() + "' created successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Clone failed: " + e.getMessage());
        }
        return "redirect:/restaurant/factory";
    }

    /**
     * Update food item
     */
    @PostMapping("/update/{foodId}")
    public String updateItem(@PathVariable Long foodId,
                             @RequestParam String name,
                             @RequestParam double price,
                             @RequestParam String description,
                             @RequestParam(defaultValue = "false") boolean available,
                             RedirectAttributes ra) {
        try {
            foodFactoryService.updateFoodItem(foodId, name, price, description, available);
            ra.addFlashAttribute("success", "✏️ Item updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Update failed: " + e.getMessage());
        }
        return "redirect:/restaurant/factory";
    }

    /**
     * Delete food item
     */
    @PostMapping("/delete/{foodId}")
    public String deleteItem(@PathVariable Long foodId, RedirectAttributes ra) {
        try {
            foodFactoryService.deleteFoodItem(foodId);
            ra.addFlashAttribute("success", "🗑️ Item deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "❌ Delete failed: " + e.getMessage());
        }
        return "redirect:/restaurant/factory";
    }
}
