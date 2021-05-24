package ru.kuznetsov.loyaltymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.domain.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
    @Query("SELECT b FROM Balance b")
    Balance findByCustomerId();
}
