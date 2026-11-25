package dev.rmoran.paymentapi.payment.entity;

import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.shared.exception.InvalidPaymentStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
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

    public Payment(Customer customer, BigDecimal amount, String currency, String description, PaymentMethod paymentMethod) {
        if (customer == null) { throw  new IllegalArgumentException("Customer cannot be null"); }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) { throw  new IllegalArgumentException("Amount must be greater than zero"); }
        if (currency == null || !currency.trim().matches("(?i)[a-z]{3}")) { throw  new IllegalArgumentException("Currency must contain a valid 3-letter code"); }
        if (paymentMethod == null) { throw  new IllegalArgumentException("Payment method cannot be null"); }

        this.customer = customer;
        this.amount = amount;
        this.currency = currency.trim().toUpperCase(Locale.ROOT);
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
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
