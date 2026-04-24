package com.foodorder.controller;

import com.foodorder.model.*;
import com.foodorder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired private UserService userService;
    @Autowired private MenuService menuService;
    @Autowired private CartService cartService;
    @Autowired private OrderService orderService;

    private Customer getCustomer(Authentication auth) {
        return userService.findCustomerByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @GetMapping("/home")
    public String home(Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        List<Restaurant> restaurants = userService.getAllRestaurants();
        Cart cart = cartService.getCart(customer.getUserId());
        model.addAttribute("customer", customer);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("cartCount", cart.getItems().size());
        return "customer/home";
    }

    @GetMapping("/restaurant/{id}/menu")
    public String viewMenu(@PathVariable Long id, Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        Restaurant restaurant = userService.getAllRestaurants().stream()
                .filter(r -> r.getUserId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        List<Menu> menus = menuService.getMenusByRestaurant(id);
        Cart cart = cartService.getCart(customer.getUserId());
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menus", menus);
        model.addAttribute("cartCount", cart.getItems().size());
        model.addAttribute("customer", customer);
        return "customer/menu";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        Cart cart = cartService.getCart(customer.getUserId());
        model.addAttribute("cart", cart);
        model.addAttribute("customer", customer);
        model.addAttribute("cartCount", cart.getItems().size());
        return "customer/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long foodItemId, @RequestParam int quantity,
                            @RequestParam Long restaurantId, Authentication auth,
                            RedirectAttributes ra) {
        Customer customer = getCustomer(auth);
        cartService.addItem(customer.getUserId(), foodItemId, quantity);
        ra.addFlashAttribute("success", "Item added to cart!");
        return "redirect:/customer/restaurant/" + restaurantId + "/menu";
    }

    @PostMapping("/cart/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId, Authentication auth) {
        Customer customer = getCustomer(auth);
        cartService.removeItem(customer.getUserId(), cartItemId);
        return "redirect:/customer/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        Cart cart = cartService.getCart(customer.getUserId());
        if (cart.getItems().isEmpty()) return "redirect:/customer/home";
        model.addAttribute("cart", cart);
        model.addAttribute("customer", customer);
        model.addAttribute("cartCount", cart.getItems().size());
        return "customer/checkout";
    }

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam Long restaurantId,
                             @RequestParam String paymentType,
                             @RequestParam String deliveryAddress,
                             Authentication auth, RedirectAttributes ra) {
        try {
            Customer customer = getCustomer(auth);
            Order order = orderService.placeOrder(customer.getUserId(), restaurantId, paymentType, deliveryAddress);
            ra.addFlashAttribute("success", "Order #" + order.getOrderId() + " placed successfully!");
            return "redirect:/customer/orders";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/customer/checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        List<Order> orders = orderService.getOrdersByCustomer(customer.getUserId());
        Cart cart = cartService.getCart(customer.getUserId());
        model.addAttribute("orders", orders);
        model.addAttribute("customer", customer);
        model.addAttribute("cartCount", cart.getItems().size());
        return "customer/orders";
    }

    @GetMapping("/orders/{orderId}")
    public String trackOrder(@PathVariable Long orderId, Model model, Authentication auth) {
        Customer customer = getCustomer(auth);
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Cart cart = cartService.getCart(customer.getUserId());
        model.addAttribute("order", order);
        model.addAttribute("customer", customer);
        model.addAttribute("cartCount", cart.getItems().size());
        return "customer/track";
    }
}
