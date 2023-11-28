package com.exxeta.investmentservice.service.database;

import com.exxeta.investmentservice.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("cleardb")
public class DatabaseCleaner {

    private final AccountMovementRepository accountMovementRepository;
    private final DepotEntryRepository depotEntryRepository;
    private final InvestmentRepository investmentRepository;
    private final ProfitRepository profitRepository;
    private final TransactionRepository transactionRepository;

    private final Logger logger = LoggerFactory.getLogger(DatabaseCleaner.class);

    public DatabaseCleaner(AccountMovementRepository accountMovementRepository,
                           DepotEntryRepository depotEntryRepository,
                           InvestmentRepository investmentRepository,
                           ProfitRepository profitRepository,
                           TransactionRepository transactionRepository) {
        this.accountMovementRepository = accountMovementRepository;
        this.depotEntryRepository = depotEntryRepository;
        this.investmentRepository = investmentRepository;
        this.profitRepository = profitRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    void setUpDatabases() {
        logger.info("Clearing databases...");
        accountMovementRepository.deleteAll();
        depotEntryRepository.deleteAll();
        investmentRepository.deleteAll();
        profitRepository.deleteAll();
        transactionRepository.deleteAll();
        logger.info("... all databases are empty");
    }
}
