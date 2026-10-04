package com.recovery.recovery.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recovery.recovery.dto.RecoveryAttemptRequest;
import com.recovery.recovery.dto.RecoveryAttemptResponse;
import com.recovery.recovery.dto.RecoveryEvaluationRequest;
import com.recovery.recovery.dto.RecoveryEvaluationResponse;
import com.recovery.recovery.service.RecoveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecoveryController {

    private final RecoveryService recoveryService;

    @PostMapping("/recovery/evaluate")
    public ResponseEntity<RecoveryEvaluationResponse> evaluateRecovery(@Valid @RequestBody RecoveryEvaluationRequest request) {
        return ResponseEntity.ok(recoveryService.evaluateRecovery(request));
    }

    @PostMapping("/recovery/attempt")
    public ResponseEntity<RecoveryAttemptResponse> logRecoveryAttempt(@Valid @RequestBody RecoveryAttemptRequest request) {
        // Ye service method tujhe RecoveryService me banana hoga
        return ResponseEntity.ok(recoveryService.logAttempt(request));
    }
}
