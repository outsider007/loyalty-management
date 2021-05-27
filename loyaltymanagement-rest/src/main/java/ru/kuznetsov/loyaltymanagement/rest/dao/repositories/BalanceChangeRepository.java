package ru.kuznetsov.loyaltymanagement.rest.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.BalanceChange;

import java.util.List;

@Repository
public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Integer> {
    List<BalanceChange> findByBalanceId(Integer balanceId);
}
