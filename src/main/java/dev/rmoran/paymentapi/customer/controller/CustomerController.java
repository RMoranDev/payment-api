package dev.rmoran.paymentapi.customer.controller;

import dev.rmoran.paymentapi.customer.dto.request.CreateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.request.UpdateCustomerRequest;
import dev.rmoran.paymentapi.customer.dto.response.CustomerResponse;
import dev.rmoran.paymentapi.customer.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable UUID id) {
        return customerService.findById(id);
    }

    @PostMapping
    public CustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PatchMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable UUID id, @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }
}
