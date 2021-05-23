package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kuznetsov.loyaltymanagement.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByFirstNameStartsWithIgnoreCase(String filterText);
}
