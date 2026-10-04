package com.recovery.recovery.entity;

/**
 * RecoveryScore
 */
//Backend ne checkout ki recovery priority calculate ki — uska result store karegi.
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.recovery.recovery.enums.Priority;

@Entity
@Table(name = "recovery_score")
@Getter @Setter @NoArgsConstructor
public class RecoveryScore {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checkout_id")
    private Checkout checkout;

    private Integer score;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Checkout getCheckout() { return checkout; }
    public void setCheckout(Checkout checkout) { this.checkout = checkout; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

}
