package com.recovery.recovery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.recovery.recovery.entity.Checkout;
import com.recovery.recovery.enums.CheckoutStatus;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Optional<Checkout> findByShopifyCheckoutId(String shopifyCheckoutId);
    List<Checkout> findByStatus(CheckoutStatus status);
}
