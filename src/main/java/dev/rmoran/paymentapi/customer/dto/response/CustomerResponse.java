package dev.rmoran.paymentapi.customer.dto.response;

import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.customer.entity.CustomerStatus;
import dev.rmoran.paymentapi.customer.entity.CustomerType;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        CustomerType customerType,
        String fullName,
        String email,
        String document,
        CustomerStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerType(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getDocument(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
