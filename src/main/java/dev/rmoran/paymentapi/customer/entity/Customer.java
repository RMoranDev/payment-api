package dev.rmoran.paymentapi.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, updatable = false, length = 20)
    private CustomerType customerType;

    @Column(name = "full_name", nullable = false, length = 150)
    @Setter
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @Setter
    private String email;

    @Column(name = "document", nullable = false, updatable = false, unique = true, length = 14)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder
    public Customer(CustomerType customerType, String fullName,
                    String email, String document) {
        this.customerType = customerType;
        this.fullName = fullName;
        this.email = email;
        this.document = document;
        this.status = CustomerStatus.ACTIVE;
    }

    public void changeStatus(CustomerStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus cannot be null");
        }

        if (this.status == newStatus) {
            throw new IllegalStateException("The record is already in " + newStatus + " status.");
        }

        this.status = newStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
