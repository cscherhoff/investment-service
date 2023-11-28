package com.exxeta.investmentservice.service;

import com.exxeta.investmentservice.dtos.InvestedInformation;
import com.exxeta.investmentservice.entities.AccountMovement;
import com.exxeta.investmentservice.entities.Investment;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.AccountMovementRepository;
import com.exxeta.investmentservice.repositories.InvestmentRepository;
import com.exxeta.investmentservice.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvestmentHandler {

    private final InvestmentRepository investmentRepository;
    private final TransactionRepository transactionRepository;

    private final AccountMovementRepository accountMovementRepository;

    public InvestmentHandler(
            InvestmentRepository investmentRepository,
            TransactionRepository transactionRepository, AccountMovementRepository accountMovementRepository) {this.investmentRepository = investmentRepository;
        this.transactionRepository = transactionRepository;
        this.accountMovementRepository = accountMovementRepository;
    }

    public void handleNewInvestment(Investment investment) {
        investmentRepository.save(investment);
    }

    public InvestedInformation getInvestedInformation(String userId) {
        BigDecimal alreadyInvested = calculateAlreadyInvested(userId).subtract(BigDecimal.valueOf(5514.8));
        BigDecimal dedicatedToInvest = calculateAmountDedicatedToInvest(userId);
        BigDecimal investable = dedicatedToInvest.subtract(alreadyInvested);

        return new InvestedInformation(dedicatedToInvest, alreadyInvested, investable);
    }

    private BigDecimal calculateAlreadyInvested(String userId) {
        List<AccountMovement> accountMovementList = accountMovementRepository.findAllByUserId(userId);
        BigDecimal alreadyInvested = BigDecimal.ZERO;
        for (AccountMovement accountMovement: accountMovementList) {
            if(accountMovement.getType().equals("TransferFromDepot")) {
                alreadyInvested = alreadyInvested.subtract(accountMovement.getAmount());
            } else if (accountMovement.getType().equals("TransferToDepot")) {
                alreadyInvested = alreadyInvested.add(accountMovement.getAmount());
            }
        }
        return alreadyInvested;
    }

    private BigDecimal calculateAmountDedicatedToInvest(String userId) {
        List<Investment> investmentList = investmentRepository.findAllByUserId(userId);
        BigDecimal dedicatedToInvest = BigDecimal.ZERO;
        for (Investment investment: investmentList) {
            dedicatedToInvest = dedicatedToInvest.add(BigDecimal.valueOf(investment.amount));
        }
        return dedicatedToInvest;
    }

//    @PostConstruct
//    public void fillDatabase() {
//        List<Investment> investmentList = new ArrayList<>();
//
//        for (int i=0; i<40; i++) {
//            LocalDate date = LocalDate.of(2019, 5, 1).plusMonths(i);
//            Investment info = new Investment(1234567L, date, 800);
//            investmentList.add(info);
//        }
//        investmentRepository.saveAll(investmentList);
//    }
}
