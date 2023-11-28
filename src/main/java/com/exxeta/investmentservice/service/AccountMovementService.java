package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.AccountMovement;
import com.exxeta.investmentservice.repositories.AccountMovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountMovementService {
    private final AccountMovementRepository accountMovementRepository;

    public AccountMovementService(AccountMovementRepository accountMovementRepository) {
        this.accountMovementRepository = accountMovementRepository;
    }

    public AccountMovement insertAccountMovementToDatabase(AccountMovement accountMovement) {
        return accountMovementRepository.save(accountMovement);
    }

    public List<AccountMovement> getAllAccountMovements(String userId) {
        return accountMovementRepository.findAllByUserId(userId);
    }
}
