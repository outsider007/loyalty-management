package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.domain.BalanceChange;

@Repository
public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Integer> {
}
