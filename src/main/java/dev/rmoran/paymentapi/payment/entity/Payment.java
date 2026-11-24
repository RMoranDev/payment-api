package dev.rmoran.paymentapi.payment.entity;

import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.shared.exception.InvalidPaymentStateException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    private void validatePendingStatus() {
        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException("Payment status is " + status);
        }
    }

    public void approve() {
        validatePendingStatus();
        this.status = PaymentStatus.APPROVED;
        this.paidAt = Instant.now();
    }

    public void reject() {
        validatePendingStatus();
        this.status = PaymentStatus.REJECTED;

    }

    public void cancel() {
        validatePendingStatus();
        this.status = PaymentStatus.CANCELLED;

    }
}
