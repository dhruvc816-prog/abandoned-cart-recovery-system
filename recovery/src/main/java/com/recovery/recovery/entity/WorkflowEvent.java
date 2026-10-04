package com.recovery.recovery.entity;

/**
 * WorkflowEvent
 * Automation ke andar kya-kya hua uska event log maintain karna.
 */
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.recovery.recovery.enums.EventType;

@Entity
@Table(name = "workflow_event")
@Getter @Setter @NoArgsConstructor
public class WorkflowEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checkout_id")
    private Checkout checkout;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String eventData;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Checkout getCheckout() { return checkout; }
    public void setCheckout(Checkout checkout) { this.checkout = checkout; }
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }
}
