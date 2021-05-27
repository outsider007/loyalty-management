package ru.kuznetsov.loyaltymanagement.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kuznetsov.loyaltymanagement.web.domain.BalanceChange;

import java.util.List;

@Repository
public interface BalanceChangeRepository extends JpaRepository<BalanceChange, Integer> {
    List<BalanceChange> findByBalanceId(Integer balanceId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BalanceChange b where b.balanceId=?1")
    void deleteByBalanceId(Integer balanceId);
}
