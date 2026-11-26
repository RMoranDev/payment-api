package dev.rmoran.paymentapi.payment.controller;

import dev.rmoran.paymentapi.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController (PaymentService paymentService) { this.paymentService = paymentService; }


}
