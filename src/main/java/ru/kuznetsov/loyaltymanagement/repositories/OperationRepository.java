package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kuznetsov.loyaltymanagement.domain.Operation;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
