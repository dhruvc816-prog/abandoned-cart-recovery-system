package com.recovery.recovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.recovery.recovery.enums.Channel;

public record RecoveryAttemptRequest(
        @NotNull Long checkoutId,
        @NotNull @Positive Integer attemptNumber,
        @NotNull Channel channel,
        @NotBlank String message
) {
}
