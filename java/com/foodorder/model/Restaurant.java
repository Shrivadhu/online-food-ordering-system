package com.foodorder.model;

import com.foodorder.enums.UserRole;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@PrimaryKeyJoinColumn(name = "user_id")
public class Restaurant extends User {

    private String restaurantName;
    private String location;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Restaurant() {
        setRole(UserRole.RESTAURANT);
    }

    public Restaurant(String name, String email, String password, String restaurantName, String location) {
        super(name, email, password, UserRole.RESTAURANT);
        this.restaurantName = restaurantName;
        this.location = location;
    }

    public void addFoodItem(FoodItem item) { /* add item to menu */ }
    public void updateFood(FoodItem item) { /* update food item */ }
    public void acceptOrder(Order order) { /* accept order */ }
    public void rejectOrder(Order order) { /* reject order */ }

    // Getters and Setters
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Menu> getMenus() { return menus; }
    public void setMenus(List<Menu> menus) { this.menus = menus; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
