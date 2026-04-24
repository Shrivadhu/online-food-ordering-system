package com.foodorder.controller;

import com.foodorder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "auth/register";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@RequestParam String name, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String address,
                                   @RequestParam String phone, RedirectAttributes ra) {
        try {
            userService.registerCustomer(name, email, password, address, phone);
            ra.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @PostMapping("/register/restaurant")
    public String registerRestaurant(@RequestParam String name, @RequestParam String email,
                                     @RequestParam String password, @RequestParam String restaurantName,
                                     @RequestParam String location, RedirectAttributes ra) {
        try {
            userService.registerRestaurant(name, email, password, restaurantName, location);
            ra.addFlashAttribute("success", "Restaurant registered! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @PostMapping("/register/delivery")
    public String registerDelivery(@RequestParam String name, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String vehicleNo,
                                   RedirectAttributes ra) {
        try {
            userService.registerDeliveryPerson(name, email, password, vehicleNo);
            ra.addFlashAttribute("success", "Delivery account created! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
