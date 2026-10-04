package com.recovery.recovery.dto;

import com.recovery.recovery.enums.CustomerSegment;
import com.recovery.recovery.enums.Priority;

public record RecoveryEvaluationResponse(
        Boolean eligible,
        Integer score,
        Priority priority,
        CustomerSegment segment,
        String reason
) {
}
