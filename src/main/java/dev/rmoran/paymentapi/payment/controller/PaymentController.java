package dev.rmoran.paymentapi.payment.controller;

import dev.rmoran.paymentapi.payment.dto.request.CreatePaymentRequest;
import dev.rmoran.paymentapi.payment.dto.response.PaymentResponse;
import dev.rmoran.paymentapi.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController (PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }
}
