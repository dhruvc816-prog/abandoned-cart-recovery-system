package com.recovery.recovery.dto;

import java.time.LocalDateTime;

import com.recovery.recovery.enums.EventType;

public record WorkflowEventResponse(
        Long id,
        Long checkoutId,
        EventType eventType,
        String eventData,
        LocalDateTime createdAt
) {
}
