package com.foodorder.config;

import com.foodorder.enums.DeliveryStatus;
import com.foodorder.model.*;
import com.foodorder.pattern.factory.FoodItemFactory;
import com.foodorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private DeliveryPersonRepository deliveryPersonRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private FoodItemFactory foodItemFactory;

    @Override
    public void run(String... args) {
        // Admin
        Admin admin = new Admin("Admin User", "admin@food.com", passwordEncoder.encode("admin123"));
        userRepository.save(admin);

        // Restaurants
        Restaurant r1 = new Restaurant("Raj Kumar", "spicegardenrest@food.com", passwordEncoder.encode("rest123"), "Spice Garden", "Indiranagar, Bangalore");
        Restaurant r2 = new Restaurant("Priya Sharma", "pizzaplanetrest@food.com", passwordEncoder.encode("rest123"), "Pizza Planet", "Koramangala, Bangalore");
        Restaurant r3 = new Restaurant("Ahmed Khan", "biryaniblissrest@food.com", passwordEncoder.encode("rest123"), "Biryani Bliss", "Whitefield, Bangalore");
        restaurantRepository.save(r1);
        restaurantRepository.save(r2);
        restaurantRepository.save(r3);

        // Menus & Items for Spice Garden
        Menu menu1 = new Menu("Main Course", r1);
        menuRepository.save(menu1);
        saveFoodItem("VEG", "Paneer Butter Masala", 280, "Rich creamy paneer curry", menu1);
        saveFoodItem("VEG", "Dal Tadka", 180, "Tempered yellow lentils", menu1);
        saveFoodItem("NON_VEG", "Chicken Tikka Masala", 320, "Grilled chicken in spiced gravy", menu1);
        saveFoodItem("NON_VEG", "Butter Chicken", 300, "Classic Murgh Makhani", menu1);

        Menu menu2 = new Menu("Breads & Rice", r1);
        menuRepository.save(menu2);
        saveFoodItem("VEG", "Garlic Naan", 50, "Soft leavened bread with garlic butter", menu2);
        saveFoodItem("VEG", "Steamed Rice", 60, "Basmati rice, perfectly cooked", menu2);
        saveFoodItem("BEVERAGE", "Mango Lassi", 90, "Sweet mango yogurt drink", menu2);
        saveFoodItem("DESSERT", "Gulab Jamun", 80, "Soft milk dumplings in sugar syrup", menu2);

        // Menus & Items for Pizza Planet
        Menu menu3 = new Menu("Pizzas", r2);
        menuRepository.save(menu3);
        saveFoodItem("VEG", "Margherita Pizza", 250, "Classic tomato, mozzarella & basil", menu3);
        saveFoodItem("VEG", "Farmhouse Pizza", 320, "Loaded with fresh veggies", menu3);
        saveFoodItem("NON_VEG", "Chicken BBQ Pizza", 380, "Smoky BBQ chicken with onions", menu3);
        saveFoodItem("NON_VEG", "Pepperoni Pizza", 400, "Italian pepperoni with cheese", menu3);

        Menu menu4 = new Menu("Sides & Drinks", r2);
        menuRepository.save(menu4);
        saveFoodItem("VEG", "Garlic Bread", 120, "Toasted with herb butter", menu4);
        saveFoodItem("VEG", "Coleslaw", 80, "Creamy cabbage salad", menu4);
        saveFoodItem("BEVERAGE", "Cold Coffee", 110, "Chilled blended coffee", menu4);
        saveFoodItem("DESSERT", "Choco Lava Cake", 150, "Warm chocolate lava cake", menu4);

        // Menus & Items for Biryani Bliss
        Menu menu5 = new Menu("Biryanis", r3);
        menuRepository.save(menu5);
        saveFoodItem("VEG", "Veg Dum Biryani", 220, "Fragrant basmati with mixed vegetables", menu5);
        saveFoodItem("NON_VEG", "Chicken Biryani", 280, "Hyderabadi style dum biryani", menu5);
        saveFoodItem("NON_VEG", "Mutton Biryani", 360, "Slow cooked mutton biryani", menu5);
        saveFoodItem("NON_VEG", "Prawn Biryani", 400, "Coastal spiced prawn biryani", menu5);

        Menu menu6 = new Menu("Accompaniments", r3);
        menuRepository.save(menu6);
        saveFoodItem("VEG", "Mirchi Ka Salan", 90, "Hyderabadi chili curry", menu6);
        saveFoodItem("VEG", "Raita", 60, "Cooling yogurt with veggies", menu6);
        saveFoodItem("BEVERAGE", "Rose Sharbat", 70, "Chilled rose syrup drink", menu6);
        saveFoodItem("DESSERT", "Double Ka Meetha", 120, "Bread pudding Hyderabadi style", menu6);

        // Customers
        Customer c1 = new Customer("Arjun Mehta", "arjun@customer.com", passwordEncoder.encode("cust123"), "12 MG Road, Bangalore", "9876543210");
        Customer c2 = new Customer("Sneha Nair", "sneha@customer.com", passwordEncoder.encode("cust123"), "45 HSR Layout, Bangalore", "9876543211");
        customerRepository.save(c1);
        customerRepository.save(c2);

        // Delivery Persons
        DeliveryPerson dp1 = new DeliveryPerson("Ravi Kumar", "ravi@delivery.com", passwordEncoder.encode("del123"), "KA-01-AB-1234");
        DeliveryPerson dp2 = new DeliveryPerson("Suresh Babu", "suresh@delivery.com", passwordEncoder.encode("del123"), "KA-02-CD-5678");
        deliveryPersonRepository.save(dp1);
        deliveryPersonRepository.save(dp2);

        System.out.println("\n========================================");
        System.out.println("  FOOD ORDER SYSTEM - DEMO CREDENTIALS");
        System.out.println("========================================");
        System.out.println("  Admin:      admin@food.com / admin123");
        System.out.println("  Customer:   arjun@customer.com / cust123");
        System.out.println("  Restaurant: spicegardenrest@food.com / rest123");
        System.out.println("  Delivery:   ravi@delivery.com / del123");
        System.out.println("========================================\n");
    }

    private void saveFoodItem(String type, String name, double price, String desc, Menu menu) {
        FoodItem item = foodItemFactory.createFoodItem(type, name, price, desc);
        item.setMenu(menu);
        foodItemRepository.save(item);
    }
}
