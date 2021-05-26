package ru.kuznetsov.loyaltymanagement.rest.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<ru.kuznetsov.loyaltymanagement.rest.dao.domain.Customer, Integer> {
    List<Customer> findByFirstNameStartingWith(String firstName);
}
