package com.recovery.recovery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recovery.recovery.dto.CheckoutRecoveryResponse;
import com.recovery.recovery.dto.CheckoutStatusResponse;
import com.recovery.recovery.service.CheckoutService;
import com.recovery.recovery.service.RecoveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckoutController {

    private final RecoveryService recoveryService;
    private final CheckoutService checkoutService;

    @GetMapping("/checkout/{id}/status")
    public ResponseEntity<CheckoutStatusResponse> getCheckoutStatus(@PathVariable Long id) {
        return ResponseEntity.ok(checkoutService.getCheckoutStatus(id));
    }

    @GetMapping("/recovery/{checkoutId}")
    public ResponseEntity<CheckoutRecoveryResponse> getCheckoutRecoveryStatus(@PathVariable Long checkoutId) {
        return ResponseEntity.ok(recoveryService.getCheckoutRecoveryStatus(checkoutId));
    }
}
