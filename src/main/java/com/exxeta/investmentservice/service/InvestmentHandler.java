package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.dtos.InvestedInformation;
import com.exxeta.investmentservice.entities.Investment;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.InvestmentRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvestmentHandler {

    private final InvestmentRepository investmentRepository;
    private final TransactionRepository transactionRepository;

    public InvestmentHandler(
        InvestmentRepository investmentRepository,
        TransactionRepository transactionRepository) {this.investmentRepository = investmentRepository;
        this.transactionRepository = transactionRepository;
    }

    public void handleNewInvestment(Investment investment) {
        investmentRepository.save(investment);
    }

    public InvestedInformation getInvestedInformation(long userId) {
        BigDecimal alreadyInvested = calculateAlreadyInvested(userId);
        BigDecimal dedicatedToInvest = calculateAmountDedicatedToInvest(userId);
        BigDecimal investable = dedicatedToInvest.subtract(alreadyInvested.subtract(BigDecimal.valueOf(5514.8)));

        return new InvestedInformation(dedicatedToInvest, alreadyInvested, investable);
    }

    private BigDecimal calculateAlreadyInvested(long userId) {
        List<Transaction> transactionList = transactionRepository.findAllByUserId(userId);
        BigDecimal alreadyInvested = BigDecimal.ZERO;
        for (Transaction transaction: transactionList) {
            if(transaction.getType().equals("TransferFromDepot")) {
                alreadyInvested = alreadyInvested.subtract(transaction.getTotalPrice());
            } else if (transaction.getType().equals("TransferToDepot")) {
                alreadyInvested = alreadyInvested.add(transaction.getTotalPrice());
            }
        }
        return alreadyInvested;
    }

    private BigDecimal calculateAmountDedicatedToInvest(long userId) {
        List<Investment> investmentList = investmentRepository.findAllByUserId(userId);
        BigDecimal dedicatedToInvest = BigDecimal.ZERO;
        for (Investment investment: investmentList) {
            dedicatedToInvest = dedicatedToInvest.add(BigDecimal.valueOf(investment.amount));
        }
        return dedicatedToInvest;
    }
}
