package com.recovery.recovery.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.recovery.recovery.dto.CheckoutStatusResponse;
import com.recovery.recovery.entity.Checkout;
import com.recovery.recovery.entity.Customer;
import com.recovery.recovery.enums.CheckoutStatus;
import com.recovery.recovery.repository.CheckoutRepository;

@Service
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    public CheckoutService(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    public Optional<Checkout> findById(Long id) {
        return checkoutRepository.findById(id);
    }

    public CheckoutStatusResponse getCheckoutStatus(Long id) {
        Checkout checkout = checkoutRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkout not found"));

        return new CheckoutStatusResponse(
                checkout.getId(),
                checkout.getStatus() == null ? null : checkout.getStatus().name());
    }

    public Optional<Checkout> findByShopifyCheckoutId(String shopifyCheckoutId) {
        return checkoutRepository.findByShopifyCheckoutId(shopifyCheckoutId);
    }

    public Checkout createCheckout(String shopifyCheckoutId, Customer customer, BigDecimal totalAmount, String currency) {
        Checkout checkout = new Checkout();
        checkout.setShopifyCheckoutId(shopifyCheckoutId);
        checkout.setCustomer(customer);
        checkout.setTotalAmount(totalAmount);
        checkout.setCurrency(currency);
        checkout.setStatus(CheckoutStatus.ABANDONED);
        checkout.setAbandonedAt(LocalDateTime.now());
        return checkoutRepository.save(checkout);
    }

    public Checkout updateCheckoutStatus(Checkout checkout, CheckoutStatus status) {
        checkout.setStatus(status);
        if (status == CheckoutStatus.RECOVERED) {
            checkout.setRecoveredAt(LocalDateTime.now());
        }
        return checkoutRepository.save(checkout);
    }

    public Checkout markAsRecovered(Checkout checkout) {
        checkout.setStatus(CheckoutStatus.RECOVERED);
        checkout.setRecoveredAt(LocalDateTime.now());
        return checkoutRepository.save(checkout);
    }

    public boolean isAlreadyRecovered(Checkout checkout) {
        return checkout.getStatus() == CheckoutStatus.RECOVERED;
    }
}
