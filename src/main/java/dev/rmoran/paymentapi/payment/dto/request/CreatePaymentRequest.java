package dev.rmoran.paymentapi.payment.dto.request;

import dev.rmoran.paymentapi.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull
        UUID customerId,

        @Positive
        @NotNull
        BigDecimal amount,

        @NotBlank
        @Pattern(regexp = "(?i)^[a-z]{3}$")
        String currency,

        String description,

        @NotNull
        PaymentMethod paymentMethod
) {
}
