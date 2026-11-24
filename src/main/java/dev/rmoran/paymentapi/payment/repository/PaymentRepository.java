package dev.rmoran.paymentapi.payment.repository;

import dev.rmoran.paymentapi.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
