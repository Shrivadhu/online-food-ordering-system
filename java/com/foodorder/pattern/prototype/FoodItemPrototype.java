package com.foodorder.pattern.prototype;

/**
 * PROTOTYPE PATTERN (Creational) - Concrete Prototype
 * FoodItemPrototype enables cloning food items to create variations
 * (e.g., a "Paneer Burger" cloned from "Chicken Burger" template)
 * without re-instantiating from scratch.
 *
 * Used by FoodFactory portal to clone existing items as templates.
 */
public class FoodItemPrototype implements Cloneable<FoodItemPrototype> {

    private String name;
    private double price;
    private String category;
    private String description;
    private String imageUrl;
    private boolean available;

    public FoodItemPrototype() {}

    public FoodItemPrototype(String name, double price, String category,
                              String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.available = true;
    }

    @Override
    public FoodItemPrototype cloneObject() {
        FoodItemPrototype clone = new FoodItemPrototype();
        clone.setName(this.name + " (Copy)");
        clone.setPrice(this.price);
        clone.setCategory(this.category);
        clone.setDescription(this.description);
        clone.setImageUrl(this.imageUrl);
        clone.setAvailable(this.available);
        return clone;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
