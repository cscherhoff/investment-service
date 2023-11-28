package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findAllByUserId(String userId);
}
