package com.recovery.recovery.dto;

import java.time.LocalDateTime;

import com.recovery.recovery.enums.AttemptStatus;
import com.recovery.recovery.enums.Channel;

public record RecoveryAttemptResponse(
        Long id,
        Long checkoutId,
        Integer attemptNumber,
        Channel channel,
        String message,
        LocalDateTime sentAt,
        AttemptStatus status
) {
}
