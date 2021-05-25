package ru.kuznetsov.loyaltymanagement.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.web.domain.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByFirstNameStartingWith(String firstName);
}
