package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAll();
    @Query("""
    SELECT t FROM Transaction t
    WHERE t.buyOrder.user.id = :userId
       OR t.sellOrder.user.id = :userId
""")
    List<Transaction> findByUserId(@Param("userId") UUID userId);

}
