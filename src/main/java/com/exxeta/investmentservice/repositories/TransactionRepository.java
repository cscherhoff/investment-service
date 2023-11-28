package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(String userId);
    List<Transaction> findAllByUserIdOrderByDate(String userId);
}
