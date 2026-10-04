package com.recovery.recovery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.recovery.recovery.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByShopifyCustomerId(String shopifyCustomerId);
}
