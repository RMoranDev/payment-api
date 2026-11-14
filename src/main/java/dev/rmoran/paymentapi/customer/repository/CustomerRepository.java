package dev.rmoran.paymentapi.customer.repository;

import dev.rmoran.paymentapi.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);
    boolean existByEmailIgnoreCaseAndIdNot(String email);

}
