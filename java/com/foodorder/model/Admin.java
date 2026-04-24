package com.foodorder.model;

import com.foodorder.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    public Admin() {
        setRole(UserRole.ADMIN);
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, UserRole.ADMIN);
    }

    public void manageRestaurants() { /* manage restaurants */ }
    public void manageUsers() { /* manage users */ }
    public void generateReport() { /* generate report */ }
}
