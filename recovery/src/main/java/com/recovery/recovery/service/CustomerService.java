package com.recovery.recovery.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recovery.recovery.entity.Customer;
import com.recovery.recovery.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByShopifyCustomerId(String shopifyCustomerId) {
        return customerRepository.findByShopifyCustomerId(shopifyCustomerId);
    }

    public Customer createCustomer(String shopifyCustomerId, String email, String name) {
        Customer customer = new Customer();
        customer.setShopifyCustomerId(shopifyCustomerId);
        customer.setEmail(email);
        customer.setName(name);
        return customerRepository.save(customer);
    }

    public Customer getOrCreateCustomer(String shopifyCustomerId, String email, String name) {
        return customerRepository.findByShopifyCustomerId(shopifyCustomerId)
                .map(existing -> {
                    boolean changed = false;

                    if (existing.getEmail() == null || !Objects.equals(existing.getEmail(), email)) {
                        existing.setEmail(email);
                        changed = true;
                    }

                    if (existing.getName() == null || !Objects.equals(existing.getName(), name)) {
                        existing.setName(name);
                        changed = true;
                    }

                    return changed ? customerRepository.save(existing) : existing;
                })
                .orElseGet(() -> createCustomer(shopifyCustomerId, email, name));
    }
}
