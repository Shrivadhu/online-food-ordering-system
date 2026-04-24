package com.foodorder.controller;

import com.foodorder.enums.OrderStatus;
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
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired private UserService userService;
    @Autowired private MenuService menuService;
    @Autowired private OrderService orderService;

    private Restaurant getRestaurant(Authentication auth) {
        return userService.findRestaurantByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Restaurant restaurant = getRestaurant(auth);
        List<Order> orders = orderService.getOrdersByRestaurant(restaurant.getUserId());
        List<FoodItem> items = menuService.getFoodItemsByRestaurant(restaurant.getUserId());
        long pending = orders.stream().filter(o -> o.getStatus() == OrderStatus.PLACED).count();
        long confirmed = orders.stream().filter(o -> o.getStatus() == OrderStatus.CONFIRMED).count();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("recentOrders", orders.stream().limit(5).toList());
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("pendingOrders", pending);
        model.addAttribute("confirmedOrders", confirmed);
        model.addAttribute("totalItems", items.size());
        return "restaurant/dashboard";
    }

    @GetMapping("/orders")
    public String orders(Model model, Authentication auth) {
        Restaurant restaurant = getRestaurant(auth);
        List<Order> orders = orderService.getOrdersByRestaurant(restaurant.getUserId());
        model.addAttribute("orders", orders);
        model.addAttribute("restaurant", restaurant);
        return "restaurant/orders";
    }

    @PostMapping("/orders/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId, Authentication auth, RedirectAttributes ra) {
        orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
        ra.addFlashAttribute("success", "Order #" + orderId + " accepted.");
        return "redirect:/restaurant/orders";
    }

    @PostMapping("/orders/{orderId}/reject")
    public String rejectOrder(@PathVariable Long orderId, Authentication auth, RedirectAttributes ra) {
        orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        ra.addFlashAttribute("success", "Order #" + orderId + " rejected.");
        return "redirect:/restaurant/orders";
    }

    @PostMapping("/orders/{orderId}/prepared")
    public String markPrepared(@PathVariable Long orderId, RedirectAttributes ra) {
        orderService.updateOrderStatus(orderId, OrderStatus.PREPARED);
        ra.addFlashAttribute("success", "Order #" + orderId + " marked as prepared.");
        return "redirect:/restaurant/orders";
    }

    @GetMapping("/menu")
    public String menuManagement(Model model, Authentication auth) {
        Restaurant restaurant = getRestaurant(auth);
        List<Menu> menus = menuService.getMenusByRestaurant(restaurant.getUserId());
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menus", menus);
        return "restaurant/menu";
    }

    @PostMapping("/menu/create")
    public String createMenu(@RequestParam String menuName, Authentication auth, RedirectAttributes ra) {
        Restaurant restaurant = getRestaurant(auth);
        menuService.createMenu(restaurant.getUserId(), menuName);
        ra.addFlashAttribute("success", "Menu created successfully.");
        return "redirect:/restaurant/menu";
    }

    @PostMapping("/menu/{menuId}/add-item")
    public String addItem(@PathVariable Long menuId,
                          @RequestParam String type, @RequestParam String name,
                          @RequestParam double price, @RequestParam String description,
                          RedirectAttributes ra) {
        menuService.addFoodItem(menuId, type, name, price, description);
        ra.addFlashAttribute("success", "Food item added.");
        return "redirect:/restaurant/menu";
    }

    @PostMapping("/menu/item/{foodId}/delete")
    public String deleteItem(@PathVariable Long foodId, RedirectAttributes ra) {
        menuService.deleteFoodItem(foodId);
        ra.addFlashAttribute("success", "Item removed.");
        return "redirect:/restaurant/menu";
    }

    @PostMapping("/menu/item/{foodId}/update")
    public String updateItem(@PathVariable Long foodId, @RequestParam String name,
                             @RequestParam double price, @RequestParam String description,
                             @RequestParam(defaultValue = "false") boolean available,
                             RedirectAttributes ra) {
        menuService.updateFoodItem(foodId, name, price, description, available);
        ra.addFlashAttribute("success", "Item updated.");
        return "redirect:/restaurant/menu";
    }
}
