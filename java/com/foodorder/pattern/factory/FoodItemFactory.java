package com.foodorder.pattern.factory;

import com.foodorder.model.FoodItem;
import org.springframework.stereotype.Component;

/**
 * FACTORY PATTERN (Creational)
 * Creates FoodItem objects without exposing instantiation logic.
 * Centralizes food item creation and allows extension for specialized food types.
 */
@Component
public class FoodItemFactory {

    public FoodItem createFoodItem(String type, String name, double price, String description) {
        FoodItem item = new FoodItem();
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        item.setAvailable(true);

        switch (type.toUpperCase()) {
            case "VEG":
                item.setCategory("Vegetarian");
                item.setImageUrl("/images/veg.png");
                break;
            case "NON_VEG":
                item.setCategory("Non-Vegetarian");
                item.setImageUrl("/images/nonveg.png");
                break;
            case "BEVERAGE":
                item.setCategory("Beverage");
                item.setImageUrl("/images/beverage.png");
                break;
            case "DESSERT":
                item.setCategory("Dessert");
                item.setImageUrl("/images/dessert.png");
                break;
            default:
                item.setCategory("Other");
                item.setImageUrl("/images/food.png");
        }
        return item;
    }
}
