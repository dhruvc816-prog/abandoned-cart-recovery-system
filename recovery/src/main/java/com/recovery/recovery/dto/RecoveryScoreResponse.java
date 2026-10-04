package com.recovery.recovery.dto;

import java.time.LocalDateTime;

import com.recovery.recovery.enums.Priority;

public record RecoveryScoreResponse(
        Long id,
        Long checkoutId,
        Integer score,
        Priority priority,
        String reason,
        LocalDateTime createdAt
) {
}
