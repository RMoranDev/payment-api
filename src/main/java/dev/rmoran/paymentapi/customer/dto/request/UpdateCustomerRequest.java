package dev.rmoran.paymentapi.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @Size(min = 3, max = 150)
        @Pattern(regexp = ".*\\S.*", message = "Full name must not be blank")
        String fullName,

        @Email
        @Size(min = 3, max = 150)
        String email
) {
}
