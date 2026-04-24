package com.foodorder.service;

import com.foodorder.enums.UserRole;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private DeliveryPersonRepository deliveryPersonRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Customer registerCustomer(String name, String email, String password, String address, String phone) {
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already registered");
        Customer c = new Customer(name, email, passwordEncoder.encode(password), address, phone);
        return customerRepository.save(c);
    }

    public Restaurant registerRestaurant(String name, String email, String password, String restaurantName, String location) {
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already registered");
        Restaurant r = new Restaurant(name, email, passwordEncoder.encode(password), restaurantName, location);
        return restaurantRepository.save(r);
    }

    public DeliveryPerson registerDeliveryPerson(String name, String email, String password, String vehicleNo) {
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already registered");
        DeliveryPerson d = new DeliveryPerson(name, email, passwordEncoder.encode(password), vehicleNo);
        return deliveryPersonRepository.save(d);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Restaurant> findRestaurantByEmail(String email) {
        return restaurantRepository.findByEmail(email);
    }

    public Optional<DeliveryPerson> findDeliveryPersonByEmail(String email) {
        return deliveryPersonRepository.findByEmail(email);
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<DeliveryPerson> getAllDeliveryPersons() {
        return deliveryPersonRepository.findAll();
    }

    public long getTotalUsers() { return userRepository.count(); }
    public long getTotalRestaurants() { return restaurantRepository.count(); }
    public long getTotalCustomers() { return customerRepository.count(); }
    public long getTotalDeliveryPersons() { return deliveryPersonRepository.count(); }
}
