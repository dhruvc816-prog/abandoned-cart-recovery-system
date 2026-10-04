package com.recovery.recovery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recovery.recovery.entity.RecoveryScore;

public interface RecoveryScoreRepository extends JpaRepository<RecoveryScore, Long> {
    Optional<RecoveryScore> findTopByCheckoutIdOrderByCreatedAtDesc(Long checkoutId);
}
