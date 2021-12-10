package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionHandler {

    private final TransactionRepository transactionRepository;

    private final BuyHandler buyHandler;
    private final SalesHandler salesHandler;
    private final DividendHandler dividendHandler;

    private final Logger logger = LoggerFactory.getLogger(TransactionHandler.class);

    public TransactionHandler(TransactionRepository transactionRepository, BuyHandler buyHandler,
                              SalesHandler salesHandler, DividendHandler dividendHandler) {
        this.transactionRepository = transactionRepository;
        this.buyHandler = buyHandler;
        this.salesHandler = salesHandler;
        this.dividendHandler = dividendHandler;
    }

    public void handleTransaction(Transaction transaction) {
        /* switch case for type of transaction
          --> Buy: Save to Depot-DB
          --> Sell: remove from Depot, calculate the profit and save to profit-DB
          --> Dividend: Save to Depot-DB
         */
        //TODO: Use Enum-Class for type
        switch (transaction.getType()) {
            case "Buy":
                buyHandler.processBuy(transaction);
                break;
            case "Sell":
                salesHandler.processSale(transaction);
                break;
            case "Dividend":
                dividendHandler.processDividend(transaction);
                break;
            default:
                break;
        }
        saveTransactionToDatabase(transaction);
    }

    private void saveTransactionToDatabase(Transaction transaction) {
        transactionRepository.save(transaction);
        logger.info("Successfully saved the transaction to the database.");
    }
}
