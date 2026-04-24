package com.foodorder.model;

import com.foodorder.enums.DeliveryStatus;
import com.foodorder.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "delivery_persons")
@PrimaryKeyJoinColumn(name = "user_id")
public class DeliveryPerson extends User {

    private String vehicleNo;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_order_id")
    private Order currentOrder;

    public DeliveryPerson() {
        setRole(UserRole.DELIVERY_PERSON);
        this.deliveryStatus = DeliveryStatus.DELIVERED; // available by default
    }

    public DeliveryPerson(String name, String email, String password, String vehicleNo) {
        super(name, email, password, UserRole.DELIVERY_PERSON);
        this.vehicleNo = vehicleNo;
        this.deliveryStatus = DeliveryStatus.DELIVERED;
    }

    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }

    // Getters and Setters
    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }

    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public Order getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }
}
