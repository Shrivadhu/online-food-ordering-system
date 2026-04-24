package com.foodorder.repository;

import com.foodorder.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    @Query("SELECT r FROM Refund r LEFT JOIN FETCH r.payment p LEFT JOIN FETCH p.order o LEFT JOIN FETCH o.customer WHERE p.paymentId = :paymentId")
    Optional<Refund> findByPaymentPaymentId(@Param("paymentId") Long paymentId);

    @Query("SELECT r FROM Refund r LEFT JOIN FETCH r.payment p LEFT JOIN FETCH p.order o LEFT JOIN FETCH o.customer ORDER BY r.refundDate DESC")
    List<Refund> findAllByOrderByRefundDateDesc();
}
