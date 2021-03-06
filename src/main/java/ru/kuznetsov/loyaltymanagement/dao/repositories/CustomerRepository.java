package ru.kuznetsov.loyaltymanagement.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.dao.domain.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByPhoneNumberStartingWithOrFirstNameStartingWith(String filter, String filtrate);
}
