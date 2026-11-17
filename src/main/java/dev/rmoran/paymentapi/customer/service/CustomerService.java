package dev.rmoran.paymentapi.customer.service;

import dev.rmoran.paymentapi.customer.dto.request.CreateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.request.UpdateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.response.CustomerResponse;
import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.customer.entity.CustomerType;
import dev.rmoran.paymentapi.customer.repository.CustomerRepository;
import dev.rmoran.paymentapi.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return toResponse(foundCustomer);
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String email = normalizeEmail(request.email());
        String document = normalizeDocument(request.document());

        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        if (customerRepository.existsByDocument(document)) {
            throw new DocumentAlreadyExistsException(document);
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
            throw new EmptyUpdateRequestException();
        }

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (request.fullName() != null) {
            existingCustomer.rename(request.fullName().trim());
        }

        if (request.email() != null) {
            String email = normalizeEmail(request.email());

            if (customerRepository.existsByEmailAndIdNot(email, id)) {
                throw new EmailAlreadyExistsException(email);
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
            throw new InvalidDocumentException("Document must have 11 digits (CPF) or 14 digits (CNPJ)");
        }
    }
}
