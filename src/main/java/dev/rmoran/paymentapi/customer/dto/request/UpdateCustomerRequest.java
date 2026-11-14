package dev.rmoran.paymentapi.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 150)
        String fullName,

        @NotBlank(message = "E-mail is required")
        @Email
        @Size(min = 3, max = 150)
        String email
) {
}
