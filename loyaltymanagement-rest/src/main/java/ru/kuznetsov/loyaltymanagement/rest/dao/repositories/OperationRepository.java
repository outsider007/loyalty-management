package ru.kuznetsov.loyaltymanagement.rest.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
