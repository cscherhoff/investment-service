package com.exxeta.investmentservice.repositories;

import com.exxeta.investmentservice.entities.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMovementRepository extends JpaRepository<AccountMovement, Long> {
    List<AccountMovement> findAllByUserId(String userId);
    List<AccountMovement> findAllByUserIdOrderByDate(String userId);
}
