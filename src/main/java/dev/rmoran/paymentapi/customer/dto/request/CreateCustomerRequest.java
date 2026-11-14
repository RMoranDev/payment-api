package dev.rmoran.paymentapi.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(

        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 150)
        String fullName,

        @NotBlank(message = "E-mail is required")
        @Email
        @Size(min = 3, max = 150)
        String email,

        @NotBlank(message = "Document is required")
        @Pattern(
                regexp = "^[0-9]{11}$|^[0-9]{14}$",
                message = "Document must contain 11 digits (CPF) or 14 digits (CNPJ)"
        )
        String document
) {
}
