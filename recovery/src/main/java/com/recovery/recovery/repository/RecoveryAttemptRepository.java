package com.recovery.recovery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recovery.recovery.entity.RecoveryAttempt;

public interface RecoveryAttemptRepository extends JpaRepository<RecoveryAttempt, Long> {
    List<RecoveryAttempt> findByCheckoutIdOrderByAttemptNumberAsc(Long checkoutId);
}