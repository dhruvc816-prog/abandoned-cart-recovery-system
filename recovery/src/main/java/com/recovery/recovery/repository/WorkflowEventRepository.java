package com.recovery.recovery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recovery.recovery.entity.WorkflowEvent;

public interface WorkflowEventRepository extends JpaRepository<WorkflowEvent, Long> {
    List<WorkflowEvent> findByCheckoutIdOrderByCreatedAtAsc(Long checkoutId);
}
