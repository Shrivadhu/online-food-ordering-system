package com.foodorder.controller;

import com.foodorder.enums.DeliveryStatus;
import com.foodorder.enums.OrderStatus;
import com.foodorder.model.*;
import com.foodorder.repository.DeliveryPersonRepository;
import com.foodorder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private DeliveryPersonRepository deliveryPersonRepository;

    private DeliveryPerson getDeliveryPerson(Authentication auth) {
        return userService.findDeliveryPersonByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        DeliveryPerson dp = getDeliveryPerson(auth);
        List<Order> myOrders = orderService.getOrdersByDeliveryPerson(dp.getUserId());
        long delivered = myOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long active = myOrders.stream().filter(o -> o.getStatus() == OrderStatus.OUT_OF_DELIVERY).count();
        model.addAttribute("dp", dp);
        model.addAttribute("myOrders", myOrders.stream().limit(5).toList());
        model.addAttribute("totalDelivered", delivered);
        model.addAttribute("activeDeliveries", active);
        return "delivery/dashboard";
    }

    @GetMapping("/orders")
    public String myOrders(Model model, Authentication auth) {
        DeliveryPerson dp = getDeliveryPerson(auth);
        List<Order> orders = orderService.getOrdersByDeliveryPerson(dp.getUserId());
        // Also show prepared orders available for pickup
        List<Order> available = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.PREPARED && o.getDeliveryPerson() == null)
                .toList();
        model.addAttribute("myOrders", orders);
        model.addAttribute("availableOrders", available);
        model.addAttribute("dp", dp);
        return "delivery/orders";
    }

    @PostMapping("/orders/{orderId}/pickup")
    public String pickupOrder(@PathVariable Long orderId, Authentication auth, RedirectAttributes ra) {
        DeliveryPerson dp = getDeliveryPerson(auth);
        orderService.assignDeliveryPerson(orderId, dp.getUserId());
        dp.setDeliveryStatus(DeliveryStatus.PICKED_UP);
        deliveryPersonRepository.save(dp);
        ra.addFlashAttribute("success", "Order #" + orderId + " picked up!");
        return "redirect:/delivery/orders";
    }

    @PostMapping("/orders/{orderId}/deliver")
    public String deliverOrder(@PathVariable Long orderId, Authentication auth, RedirectAttributes ra) {
        DeliveryPerson dp = getDeliveryPerson(auth);
        orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        dp.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryPersonRepository.save(dp);
        ra.addFlashAttribute("success", "Order #" + orderId + " marked as delivered!");
        return "redirect:/delivery/orders";
    }
}
