package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Security;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.files.InvestmentExporter;
import com.exxeta.investmentservice.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Service
public class InvestmentService {

    private final DepotEntryRepository depotEntryRepository;
    private final ProfitRepository profitRepository;
    private final SecurityRepository securityRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMovementRepository accountMovementRepository;
    private final InvestmentRepository investmentRepository;

    private final InvestmentExporter investmentExporter;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(InvestmentService.class);

    public InvestmentService(DepotEntryRepository depotEntryRepository, ProfitRepository profitRepository,
                             SecurityRepository securityRepository, TransactionRepository transactionRepository,
                             AccountMovementRepository accountMovementRepository, InvestmentRepository investmentRepository, InvestmentExporter investmentExporter) {
        this.depotEntryRepository = depotEntryRepository;
        this.profitRepository = profitRepository;
        this.securityRepository = securityRepository;
        this.transactionRepository = transactionRepository;
        this.investmentRepository = investmentRepository;
        this.investmentExporter = investmentExporter;
        this.accountMovementRepository = accountMovementRepository;
    }

    public List<DepotEntry> getAllDepotEntries(String userId) {
        return depotEntryRepository.findDepotEntriesByUserId(userId);
    }

    public List<DepotEntry> getAllDepotEntries(String userId, String depotName) {
        return depotEntryRepository.findDepotEntriesByUserIdAndDepotName(userId, depotName);
    }

    public List<Profit> getAllProfits(String userId) {
        return profitRepository.findProfitsByUserId(userId);
    }

    public List<Security> getAllSecurities(String userId) {return securityRepository.findAllByUserId(userId);}

    public List<Transaction> getAllTransactions(String userId) {return transactionRepository.findAllByUserId(userId);}

    public void downloadTransactions(String userId) throws IOException {
        investmentExporter.export(transactionRepository.findAllByUserId(userId),
                accountMovementRepository.findAllByUserId(userId),
                investmentRepository.findAllByUserId(userId),
                false);
    }
}