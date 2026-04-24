package com.foodorder.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
    }

    public void addItem(FoodItem foodItem, int quantity) {
        for (CartItem item : items) {
            if (item.getFoodItem().getFoodId().equals(foodItem.getFoodId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        CartItem newItem = new CartItem(foodItem, quantity);
        newItem.setCart(this);
        items.add(newItem);
    }

    public void removeItem(Long cartItemId) {
        items.removeIf(item -> item.getId().equals(cartItemId));
    }

    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::calculatePrice).sum();
    }

    public void clear() {
        items.clear();
    }

    // Getters and Setters
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
