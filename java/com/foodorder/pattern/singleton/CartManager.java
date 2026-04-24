package com.foodorder.pattern.singleton;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * SINGLETON PATTERN (Creational)
 * Ensures each customer has exactly ONE active cart session at a time.
 * The CartManager maintains a single instance mapping customers to their active cart IDs.
 * Spring's @Component with default scope (singleton) enforces one instance per application context.
 */
@Component
public class CartManager {

    // Maps customerId -> cartId for active sessions
    private final Map<Long, Long> activeCartSessions = new HashMap<>();

    // Private constructor — Spring manages instantiation (singleton bean)
    private static CartManager instance;

    public static synchronized CartManager getInstance() {
        // For non-Spring contexts; in Spring, injection handles this
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void registerCart(Long customerId, Long cartId) {
        activeCartSessions.put(customerId, cartId);
    }

    public Long getCartId(Long customerId) {
        return activeCartSessions.get(customerId);
    }

    public boolean hasActiveCart(Long customerId) {
        return activeCartSessions.containsKey(customerId);
    }

    public void clearCart(Long customerId) {
        activeCartSessions.remove(customerId);
    }
}
