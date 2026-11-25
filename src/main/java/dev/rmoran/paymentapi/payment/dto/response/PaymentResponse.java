package dev.rmoran.paymentapi.payment.dto.response;

import dev.rmoran.paymentapi.payment.entity.Payment;
import dev.rmoran.paymentapi.payment.entity.PaymentMethod;
import dev.rmoran.paymentapi.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID customerId,
        BigDecimal amount,
        String currency,
        String description,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        Instant createdAt,
        Instant updatedAt,
        Instant paidAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getCustomer().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getPaidAt()
        );
    }
}
