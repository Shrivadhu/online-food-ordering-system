package com.foodorder.repository;

import com.foodorder.model.DeliveryPerson;
import com.foodorder.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    Optional<DeliveryPerson> findByEmail(String email);
    List<DeliveryPerson> findByDeliveryStatus(DeliveryStatus status);
}
