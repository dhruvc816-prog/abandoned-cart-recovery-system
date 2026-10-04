package com.recovery.recovery.entity;
//Shopify checkout ki complete business state maintain karegi.
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.recovery.recovery.enums.CheckoutStatus;

@Entity
@Table(name = "checkout")
@Getter @Setter @NoArgsConstructor
public class Checkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shopifyCheckoutId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    private CheckoutStatus status;

    private LocalDateTime abandonedAt;
    private LocalDateTime recoveredAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecoveryAttempt> recoveryAttempts = new ArrayList<>();

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecoveryScore> recoveryScores = new ArrayList<>();

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowEvent> workflowEvents = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShopifyCheckoutId() { return shopifyCheckoutId; }
    public void setShopifyCheckoutId(String shopifyCheckoutId) { this.shopifyCheckoutId = shopifyCheckoutId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public CheckoutStatus getStatus() { return status; }
    public void setStatus(CheckoutStatus status) { this.status = status; }
    public LocalDateTime getAbandonedAt() { return abandonedAt; }
    public void setAbandonedAt(LocalDateTime abandonedAt) { this.abandonedAt = abandonedAt; }
    public LocalDateTime getRecoveredAt() { return recoveredAt; }
    public void setRecoveredAt(LocalDateTime recoveredAt) { this.recoveredAt = recoveredAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<RecoveryAttempt> getRecoveryAttempts() { return recoveryAttempts; }
    public void setRecoveryAttempts(List<RecoveryAttempt> recoveryAttempts) { this.recoveryAttempts = recoveryAttempts; }
    public List<RecoveryScore> getRecoveryScores() { return recoveryScores; }
    public void setRecoveryScores(List<RecoveryScore> recoveryScores) { this.recoveryScores = recoveryScores; }
    public List<WorkflowEvent> getWorkflowEvents() { return workflowEvents; }
    public void setWorkflowEvents(List<WorkflowEvent> workflowEvents) { this.workflowEvents = workflowEvents; }

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }
}