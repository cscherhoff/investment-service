package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.Profit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitRepository extends JpaRepository<Profit, Long> {
    List<Profit> findProfitsByUserId(long userId);
    List<Profit> findProfitsByUserIdAndDepotNameAndIsin(long userId, String depotName, String isin);
}
