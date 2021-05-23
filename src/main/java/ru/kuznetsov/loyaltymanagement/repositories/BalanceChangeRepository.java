package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kuznetsov.loyaltymanagement.domain.BalanceChange;

public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Integer> {
}
