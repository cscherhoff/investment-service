package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.AccountMovement;
import com.exxeta.investmentservice.entities.Security;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.files.InvestmentImporter;
import com.exxeta.investmentservice.repositories.AccountMovementRepository;
import com.exxeta.investmentservice.repositories.SecurityRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImportService {

    private final InvestmentImporter investmentImporter;
    private final TransactionRepository transactionRepository;
    private final AccountMovementRepository accountMovementRepository;

    private final SecurityRepository securityRepository;

    public ImportService(InvestmentImporter investmentImporter, TransactionRepository transactionRepository, AccountMovementRepository accountMovementRepository, SecurityRepository securityRepository) {
        this.investmentImporter = investmentImporter;
        this.transactionRepository = transactionRepository;
        this.accountMovementRepository = accountMovementRepository;
        this.securityRepository = securityRepository;
    }

    public void importTransactions() {
        final List<Transaction> transactionList = investmentImporter.importTransactionList();
        final List<AccountMovement> accountMovementList = investmentImporter.importAccountMovementList();

        Set<Security> securities = new HashSet<>();
        transactionList.forEach(transaction -> securities.add(transaction.getSecurity()));
        securityRepository.saveAll(securities);
        transactionRepository.saveAll(transactionList);
        accountMovementRepository.saveAll(accountMovementList);

    }
}
