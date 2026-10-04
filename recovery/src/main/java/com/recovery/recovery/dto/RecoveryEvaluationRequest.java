package com.recovery.recovery.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record RecoveryEvaluationRequest(
        @NotBlank String shopifyCheckoutId,
        @NotBlank String shopifyCustomerId,
        @NotBlank @Email String customerEmail,
        String customerName,
        @NotNull @Positive BigDecimal totalAmount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull Boolean returningCustomer,
        @NotNull @PositiveOrZero Integer previousPurchaseCount,
        @NotNull @PositiveOrZero Integer previousRecoveryAttempts
) {
}
