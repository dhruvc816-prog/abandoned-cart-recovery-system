package com.recovery.recovery.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.recovery.recovery.dto.CheckoutRecoveryResponse;
import com.recovery.recovery.dto.RecoveryAttemptRequest;
import com.recovery.recovery.dto.RecoveryAttemptResponse;
import com.recovery.recovery.dto.RecoveryEvaluationRequest;
import com.recovery.recovery.dto.RecoveryEvaluationResponse;
import com.recovery.recovery.dto.RecoveryScoreResponse;
import com.recovery.recovery.entity.Checkout;
import com.recovery.recovery.entity.Customer;
import com.recovery.recovery.entity.RecoveryAttempt;
import com.recovery.recovery.entity.RecoveryScore;
import com.recovery.recovery.enums.AttemptStatus;
import com.recovery.recovery.enums.CustomerSegment;
import com.recovery.recovery.enums.EventType;
import com.recovery.recovery.enums.Priority;
import com.recovery.recovery.repository.RecoveryAttemptRepository;
import com.recovery.recovery.repository.RecoveryScoreRepository;

@Service
public class RecoveryService {

    private static final int MAX_ATTEMPTS = 3;
    private static final int MIN_SCORE = 20;
    private static final int AMOUNT_THRESHOLD_VERY_HIGH = 10000;
    private static final int AMOUNT_THRESHOLD_HIGH = 5000;
    private static final int AMOUNT_THRESHOLD_MEDIUM = 2000;
    private static final int VALUE_SCORE_BASE = 10;
    private static final int VALUE_SCORE_HIGH = 20;
    private static final int VALUE_SCORE_VERY_HIGH = 30;
    private static final int VALUE_SCORE_TOP = 40;
    private static final int RETURNING_CUSTOMER_BONUS = 20;
    private static final int PURCHASE_COUNT_BONUS_HIGH = 20;
    private static final int PURCHASE_COUNT_BONUS_LOW = 10;
    private static final int RECOVERY_ATTEMPT_BONUS_NONE = 10;
    private static final int RECOVERY_ATTEMPT_BONUS_ONE = 5;
    private static final int MAX_SCORE = 100;

    private final CustomerService customerService;
    private final CheckoutService checkoutService;
    private final WorkflowEventService workflowEventService;
    private final RecoveryScoreRepository recoveryScoreRepository;
    private final RecoveryAttemptRepository recoveryAttemptRepository;

    public RecoveryService(CustomerService customerService,
                          CheckoutService checkoutService,
                          WorkflowEventService workflowEventService,
                          RecoveryScoreRepository recoveryScoreRepository,
                          RecoveryAttemptRepository recoveryAttemptRepository) {
        this.customerService = customerService;
        this.checkoutService = checkoutService;
        this.workflowEventService = workflowEventService;
        this.recoveryScoreRepository = recoveryScoreRepository;
        this.recoveryAttemptRepository = recoveryAttemptRepository;
    }

    @Transactional(readOnly = true)
    public CheckoutRecoveryResponse getCheckoutRecoveryStatus(Long checkoutId) {
        Checkout checkout = checkoutService.findById(checkoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkout not found"));

        RecoveryScore latestScore = recoveryScoreRepository.findTopByCheckoutIdOrderByCreatedAtDesc(checkoutId)
                .orElse(null);
        List<RecoveryAttempt> attempts = recoveryAttemptRepository.findByCheckoutIdOrderByAttemptNumberAsc(checkoutId);

        return toCheckoutRecoveryResponse(checkout, latestScore, attempts);
    }

    @Transactional
    public RecoveryAttemptResponse logAttempt(RecoveryAttemptRequest request) {
        Checkout checkout = checkoutService.findById(request.checkoutId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkout not found"));

        RecoveryAttempt recoveryAttempt = new RecoveryAttempt();
        recoveryAttempt.setCheckout(checkout);
        recoveryAttempt.setAttemptNumber(request.attemptNumber());
        recoveryAttempt.setChannel(request.channel());
        recoveryAttempt.setMessage(request.message());
        recoveryAttempt.setSentAt(LocalDateTime.now());
        recoveryAttempt.setStatus(AttemptStatus.SENT);

        RecoveryAttempt savedAttempt = recoveryAttemptRepository.save(recoveryAttempt);
        return toRecoveryAttemptResponse(savedAttempt);
    }

    @Transactional
    public RecoveryEvaluationResponse evaluateRecovery(RecoveryEvaluationRequest request) {
        Customer customer = customerService.getOrCreateCustomer(
                request.shopifyCustomerId(),
                request.customerEmail(),
                request.customerName());

        Checkout checkout = checkoutService.findByShopifyCheckoutId(request.shopifyCheckoutId())
                .orElseGet(() -> checkoutService.createCheckout(
                        request.shopifyCheckoutId(),
                        customer,
                        request.totalAmount(),
                        request.currency()));

        workflowEventService.logEvent(checkout, EventType.CHECKOUT_RECEIVED,
                "checkoutId=" + request.shopifyCheckoutId() + ", customerId=" + request.shopifyCustomerId());

        CustomerSegment segment = determineSegment(request);

        if (checkoutService.isAlreadyRecovered(checkout)) {
            return logEvaluationResult(checkout, segment, 0, Priority.LOW, false, "Checkout already recovered");
        }

        if (request.previousRecoveryAttempts() >= MAX_ATTEMPTS) {
            return logEvaluationResult(checkout, segment, 0, Priority.LOW, false, "Maximum recovery attempts reached");
        }

        int score = calculateScore(request);
        Priority priority = determinePriority(score);
        boolean eligible = score >= MIN_SCORE;
        String reason = buildReason(request, score, priority);

        RecoveryScore recoveryScore = new RecoveryScore();
        recoveryScore.setCheckout(checkout);
        recoveryScore.setScore(score);
        recoveryScore.setPriority(priority);
        recoveryScore.setReason(reason);
        recoveryScoreRepository.save(recoveryScore);

        return logEvaluationResult(checkout, segment, score, priority, eligible, reason);
    }

    private CheckoutRecoveryResponse toCheckoutRecoveryResponse(
            Checkout checkout,
            RecoveryScore latestScore,
            List<RecoveryAttempt> attempts) {
        return new CheckoutRecoveryResponse(
                checkout.getId(),
                checkout.getShopifyCheckoutId(),
                checkout.getStatus(),
                checkout.getTotalAmount(),
                checkout.getCurrency(),
                latestScore == null ? null : toRecoveryScoreResponse(latestScore),
                attempts == null ? List.of() : attempts.stream().map(this::toRecoveryAttemptResponse).toList());
    }

    private RecoveryScoreResponse toRecoveryScoreResponse(RecoveryScore recoveryScore) {
        return new RecoveryScoreResponse(
                recoveryScore.getId(),
                recoveryScore.getCheckout() != null ? recoveryScore.getCheckout().getId() : null,
                recoveryScore.getScore(),
                recoveryScore.getPriority(),
                recoveryScore.getReason(),
                recoveryScore.getCreatedAt());
    }

    private RecoveryAttemptResponse toRecoveryAttemptResponse(RecoveryAttempt recoveryAttempt) {
        return new RecoveryAttemptResponse(
                recoveryAttempt.getId(),
                recoveryAttempt.getCheckout() != null ? recoveryAttempt.getCheckout().getId() : null,
                recoveryAttempt.getAttemptNumber(),
                recoveryAttempt.getChannel(),
                recoveryAttempt.getMessage(),
                recoveryAttempt.getSentAt(),
                recoveryAttempt.getStatus());
    }

    private RecoveryEvaluationResponse logEvaluationResult(
            Checkout checkout,
            CustomerSegment segment,
            int score,
            Priority priority,
            boolean eligible,
            String reason) {
        workflowEventService.logEvent(checkout, EventType.RECOVERY_EVALUATED,
                "score=" + score + ", priority=" + priority + ", segment=" + segment + ", eligible=" + eligible + ", reason=" + reason);

        return new RecoveryEvaluationResponse(eligible, score, priority, segment, reason);
    }

    private int calculateScore(RecoveryEvaluationRequest request) {
        int score = 0;
        BigDecimal amount = request.totalAmount();

        if (amount.compareTo(BigDecimal.valueOf(AMOUNT_THRESHOLD_VERY_HIGH)) >= 0) {
            score += VALUE_SCORE_TOP;
        } else if (amount.compareTo(BigDecimal.valueOf(AMOUNT_THRESHOLD_HIGH)) >= 0) {
            score += VALUE_SCORE_VERY_HIGH;
        } else if (amount.compareTo(BigDecimal.valueOf(AMOUNT_THRESHOLD_MEDIUM)) >= 0) {
            score += VALUE_SCORE_HIGH;
        } else {
            score += VALUE_SCORE_BASE;
        }

        if (Boolean.TRUE.equals(request.returningCustomer())) {
            score += RETURNING_CUSTOMER_BONUS;
        }

        if (request.previousPurchaseCount() >= 3) {
            score += PURCHASE_COUNT_BONUS_HIGH;
        } else if (request.previousPurchaseCount() >= 1) {
            score += PURCHASE_COUNT_BONUS_LOW;
        }

        if (request.previousRecoveryAttempts() == 0) {
            score += RECOVERY_ATTEMPT_BONUS_NONE;
        } else if (request.previousRecoveryAttempts() == 1) {
            score += RECOVERY_ATTEMPT_BONUS_ONE;
        }

        return Math.min(MAX_SCORE, Math.max(0, score));
    }

    private Priority determinePriority(int score) {
        if (score >= 70) {
            return Priority.HIGH;
        }
        if (score >= 40) {
            return Priority.MEDIUM;
        }
        return Priority.LOW;
    }

    private CustomerSegment determineSegment(RecoveryEvaluationRequest request) {
        return Boolean.TRUE.equals(request.returningCustomer()) ? CustomerSegment.RETURNING : CustomerSegment.FIRST_TIME;
    }

    private String buildReason(RecoveryEvaluationRequest request, int score, Priority priority) {
        String customerType = Boolean.TRUE.equals(request.returningCustomer()) ? "returning customer" : "new customer";
        if (priority == Priority.HIGH) {
            return "High-value checkout from " + customerType;
        }
        if (priority == Priority.MEDIUM) {
            return "Moderate-value checkout from " + customerType;
        }
        return "Low-value checkout from " + customerType + "; score=" + score;
    }
}
