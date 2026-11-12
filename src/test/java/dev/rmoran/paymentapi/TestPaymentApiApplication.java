package dev.rmoran.paymentapi;

import org.springframework.boot.SpringApplication;

public class TestPaymentApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(PaymentApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
