package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.entities.Security;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.SecurityRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionHandler {

    private final TransactionRepository transactionRepository;

    private final BuyHandler buyHandler;
    private final SalesHandler salesHandler;
    private final DividendHandler dividendHandler;
    private final SecurityRepository securityRepository;

    private final Logger logger = LoggerFactory.getLogger(TransactionHandler.class);

    public TransactionHandler(TransactionRepository transactionRepository, BuyHandler buyHandler,
                              SalesHandler salesHandler, DividendHandler dividendHandler, SecurityRepository securityRepository) {
        this.transactionRepository = transactionRepository;
        this.buyHandler = buyHandler;
        this.salesHandler = salesHandler;
        this.dividendHandler = dividendHandler;
        this.securityRepository = securityRepository;
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
            case "TransferFromDepot":
                if (transaction.getTotalPrice().signum()==1) {
                    transaction.setTotalPrice(transaction.getTotalPrice().negate());
                }
            case "Transfer":
                if (transaction.getTotalPrice().signum()==-1) {
                    transaction.setType("TransferFromDepot");
                    transaction.setTotalPrice(transaction.getTotalPrice().negate());
                    break;
                } else if (transaction.getTotalPrice().signum()==1) {
                    transaction.setType("TransferToDepot");
                    break;
                } else {
                    System.out.println("ERROR: WRONG SIGNUM: " + transaction.getTotalPrice());
                }
            default:
                break;
        }
        if (transaction.getExpenses().signum()==-1) {
            transaction.setExpenses(transaction.getExpenses().negate());
        }
        saveTransactionToDatabase(transaction);
    }

    public String recalculateAll(long userId) {
        List<Transaction> transactionList = transactionRepository.findAllByUserIdOrderByDate(userId);
        int start = 260;
        int end = 285;
        for(int i = start; i<end; i++) {
            Transaction transaction = transactionList.get(i);
            logger.info("Handling the following transaction (" + i + "): " + transaction);
            handleTransaction(transaction);
        }
//        for (Transaction transaction: transactionList) {
//            handleTransaction(transaction);
//        }
        return "Successfully recalculated dept entries and profits for all transactions";
    }

    private void saveTransactionToDatabase(Transaction transaction) {
        Optional<Security> optionalSecurity = securityRepository.findByIsin(transaction.getIsin());
        if (!optionalSecurity.isPresent()) {
            securityRepository.save(transaction.getSecurity());
        }
        transactionRepository.save(transaction);
        logger.info("Successfully saved the transaction to the database.");
    }
}
