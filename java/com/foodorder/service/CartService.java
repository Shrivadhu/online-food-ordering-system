package com.foodorder.service;

import com.foodorder.model.*;
import com.foodorder.pattern.singleton.CartManager;
import com.foodorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private CartManager cartManager; // SINGLETON PATTERN

    @Transactional
    public Cart getOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerUserId(customerId).orElseGet(() -> {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Cart cart = new Cart(customer);
            Cart saved = cartRepository.save(cart);
            // Register in singleton CartManager
            cartManager.registerCart(customerId, saved.getCartId());
            return saved;
        });
    }

    @Transactional
    public Cart addItem(Long customerId, Long foodItemId, int quantity) {
        Cart cart = getOrCreateCart(customerId);
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
        cart.addItem(foodItem, quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(Long customerId, Long cartItemId) {
        Cart cart = getOrCreateCart(customerId);
        cart.removeItem(cartItemId);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long customerId) {
        Cart cart = getOrCreateCart(customerId);
        cart.clear();
        cartRepository.save(cart);
    }

    public Cart getCart(Long customerId) {
        return getOrCreateCart(customerId);
    }
}
