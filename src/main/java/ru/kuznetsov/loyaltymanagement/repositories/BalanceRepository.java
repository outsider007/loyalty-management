package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kuznetsov.loyaltymanagement.domain.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Integer> {
}
