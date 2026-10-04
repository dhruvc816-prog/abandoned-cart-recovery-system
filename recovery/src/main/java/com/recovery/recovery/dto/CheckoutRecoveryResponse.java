package com.recovery.recovery.dto;

import java.math.BigDecimal;
import java.util.List;

import com.recovery.recovery.enums.CheckoutStatus;

public record CheckoutRecoveryResponse(
        Long checkoutId,
        String shopifyCheckoutId,
        CheckoutStatus status,
        BigDecimal totalAmount,
        String currency,
        RecoveryScoreResponse latestScore,
        List<RecoveryAttemptResponse> attempts
) {
}
