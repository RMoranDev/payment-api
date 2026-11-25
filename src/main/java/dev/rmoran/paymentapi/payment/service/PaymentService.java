package dev.rmoran.paymentapi.payment.service;

import dev.rmoran.paymentapi.customer.dto.response.CustomerResponse;
import dev.rmoran.paymentapi.customer.entity.Customer;
import dev.rmoran.paymentapi.customer.repository.CustomerRepository;
import dev.rmoran.paymentapi.payment.dto.request.CreatePaymentRequest;
import dev.rmoran.paymentapi.payment.dto.response.PaymentResponse;
import dev.rmoran.paymentapi.payment.entity.Payment;
import dev.rmoran.paymentapi.payment.repository.PaymentRepository;
import dev.rmoran.paymentapi.shared.exception.BusinessException;
import dev.rmoran.paymentapi.shared.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.customerId()));

        if (!customer.isActive()) {
            throw new BusinessException("Inactive client");
        }

        Payment payment = new Payment(
                customer,
                request.amount(),
                request.currency(),
                request.description(),
                request.paymentMethod()
        );

        Payment createdPayment = paymentRepository.save(payment);

        return PaymentResponse.from(createdPayment);
    }

}
