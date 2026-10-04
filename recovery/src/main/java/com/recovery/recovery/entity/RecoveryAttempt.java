//Customer ko kitni recovery attempts bheji gayi aur unka result kya tha, ye track karegi.
package com.recovery.recovery.entity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

import com.recovery.recovery.enums.AttemptStatus;
import com.recovery.recovery.enums.Channel;

@Entity
@Table(name = "recovery_attempt")
@Getter @Setter @NoArgsConstructor
public class RecoveryAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checkout_id")
    private Checkout checkout;

    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private AttemptStatus status;


}
