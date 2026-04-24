package com.foodorder.controller;

import com.foodorder.enums.UserRole;
import com.foodorder.model.*;
import com.foodorder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private RefundService refundService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalRestaurants", userService.getTotalRestaurants());
        model.addAttribute("totalCustomers", userService.getTotalCustomers());
        model.addAttribute("totalDeliveryPersons", userService.getTotalDeliveryPersons());
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        model.addAttribute("recentOrders", orderService.getAllOrders().stream().limit(8).toList());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("customers", userService.findByRole(UserRole.CUSTOMER));
        model.addAttribute("restaurants", userService.getAllRestaurants());
        model.addAttribute("deliveryPersons", userService.getAllDeliveryPersons());
        return "admin/users";
    }

    @GetMapping("/orders")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model) {
        model.addAttribute("restaurants", userService.getAllRestaurants());
        return "admin/restaurants";
    }

    @GetMapping("/refunds")
    public String refunds(Model model) {
        model.addAttribute("refunds", refundService.getAllRefunds());
        return "admin/refunds";
    }

    @PostMapping("/orders/{orderId}/refund")
    public String processRefund(@PathVariable Long orderId,
                                @RequestParam String reason,
                                RedirectAttributes ra) {
        try {
            refundService.processRefund(orderId, reason);
            ra.addFlashAttribute("success", "Refund processed for Order #" + orderId);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Refund failed: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
