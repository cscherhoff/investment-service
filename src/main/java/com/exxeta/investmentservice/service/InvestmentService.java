package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Security;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.DepotEntryRepository;
import com.exxeta.investmentservice.repositories.ProfitRepository;
import com.exxeta.investmentservice.repositories.SecurityRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@org.springframework.stereotype.Service
public class InvestmentService {

    private final DepotEntryRepository depotEntryRepository;
    private final ProfitRepository profitRepository;
    private final SecurityRepository securityRepository;
    private final TransactionRepository transactionRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(InvestmentService.class);

    public InvestmentService(DepotEntryRepository depotEntryRepository,
                             ProfitRepository profitRepository, SecurityRepository securityRepository, TransactionRepository transactionRepository) {
        this.depotEntryRepository = depotEntryRepository;
        this.profitRepository = profitRepository;
        this.securityRepository = securityRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<DepotEntry> getAllDepotEntries(long userId) {
        return depotEntryRepository.findDepotEntriesByUserId(userId);
    }

    public List<DepotEntry> getAllDepotEntries(long userId, String depotName) {
        return depotEntryRepository.findDepotEntriesByUserIdAndDepotName(userId, depotName);
    }

    public List<Profit> getAllProfits(long userId) {
        return profitRepository.findProfitsByUserId(userId);
    }

    public List<Security> getAllSecurities(long userId) {return securityRepository.findAllByUserId(userId);}

    public List<Transaction> getAllTransactions(long userId) {return transactionRepository.findAllByUserId(userId);}

}