package com.recovery.recovery.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String shopifyCustomerId,
        String email,
        String name,
        String phone,
        LocalDateTime createdAt
) {
}
