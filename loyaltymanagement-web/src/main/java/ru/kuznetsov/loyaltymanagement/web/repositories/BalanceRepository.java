package ru.kuznetsov.loyaltymanagement.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.loyaltymanagement.web.domain.Balance;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Balance b WHERE b.customerId=?1")
    void deleteByCustomerId(Integer customerId);

    @Query("SELECT b FROM Balance b WHERE b.customerId=?1")
    List<Balance> findByCustomerId(Integer customerId);
}
