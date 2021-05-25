package ru.kuznetsov.loyaltymanagement.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.web.domain.BalanceChange;

@Repository
public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Integer> {
}
