package com.foodorder.model;

import com.foodorder.enums.UserRole;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    private String address;
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Customer() {
        setRole(UserRole.CUSTOMER);
    }

    public Customer(String name, String email, String password, String address, String phone) {
        super(name, email, password, UserRole.CUSTOMER);
        this.address = address;
        this.phone = phone;
    }

    public void browseMenu() { /* browse menus */ }
    public void addToCart(FoodItem item, int qty) { /* add to cart */ }
    public void placeOrder(Cart cart) { /* place order */ }
    public void trackOrder(Long orderId) { /* track order */ }

    // Getters and Setters
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
