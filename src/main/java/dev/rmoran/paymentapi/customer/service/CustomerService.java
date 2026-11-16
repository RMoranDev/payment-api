package dev.rmoran.paymentapi.customer.service;

import dev.rmoran.paymentapi.customer.dto.request.CreateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.request.UpdateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.response.CustomerResponse;
import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.customer.entity.CustomerType;
import dev.rmoran.paymentapi.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return toResponse(foundCustomer);
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String email = normalizeEmail(request.email());
        String document = normalizeDocument(request.document());

        if (customerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (customerRepository.existsByDocument(document)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Document already exists");
        }

        Customer customer = Customer.builder()
                .customerType(deriveCustomerType(document))
                .fullName(request.fullName())
                .email(email)
                .document(document)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {
        if (request.fullName() == null && request.email() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one field must be provided"
            );
        }

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        if (request.fullName() != null) {
            existingCustomer.rename(request.fullName().trim());
        }

        if (request.email() != null) {
            String email = normalizeEmail(request.email());

            if (customerRepository.existsByEmailAndIdNot(email, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            existingCustomer.changeEmail(email);
        }

        return toResponse(existingCustomer);
    }

    private CustomerResponse toResponse(Customer customer) {
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

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeDocument(String document) {
        return document.replaceAll("\\D", "");
    }

    private CustomerType deriveCustomerType(String document) {
        if (document.length() == 14) {
            return CustomerType.BUSINESS;
        } else if (document.length() == 11) {
            return CustomerType.INDIVIDUAL;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Document must have 11 digits (CPF) or 14 digits (CNPJ)");
        }
    }
}
