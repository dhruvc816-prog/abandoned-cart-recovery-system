package com.recovery.recovery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.recovery.recovery.dto.CustomerResponse;
import com.recovery.recovery.entity.Customer;
import com.recovery.recovery.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return ResponseEntity.ok(new CustomerResponse(
                customer.getId(),
                customer.getShopifyCustomerId(),
                customer.getEmail(),
                customer.getName(),
                customer.getPhone(),
                customer.getCreatedAt()));
    }
}
