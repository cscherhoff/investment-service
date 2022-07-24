package com.exxeta.investmentservice.service.profits;

import com.exxeta.investmentservice.entities.DepotEntry;
import com.exxeta.investmentservice.entities.Profit;
import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.repositories.ProfitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProfitHandler {

    private final ProfitRepository repository;
    private final ProfitCalculator profitCalculator;
    private Profit profit;

    public ProfitHandler(ProfitRepository repository, ProfitCalculator profitCalculator) {
        this.repository = repository;
        this.profitCalculator = profitCalculator;
    }

    public void createProfit(Transaction transaction, DepotEntry depotEntryFromDatabase) {
        List<Profit> profitList = repository.findProfitsByUserIdAndDepotNameAndIsin(
            transaction.getUserId(), transaction.getDepotName(), transaction.getIsin()
        );
        if (profitList.size() == 0) {
            createNewProfit(transaction, depotEntryFromDatabase);
        } else if (profitList.size() == 1) {
            updateProfit(transaction, depotEntryFromDatabase, profitList.get(0));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of profits in the database for "
                + "the user ID " + transaction.getUserId() + ", the depot with the name " + transaction.getDepotName()
                + " and the ISIN " + transaction.getIsin() + " should be zero or one, but was " + profitList.size());
        }
    }

    public void saveProfit() {
        if (profit!=null) {
            repository.save(profit);
        }
    }

    private void createNewProfit(Transaction transaction, DepotEntry depotEntry) {
        BigDecimal netAbsoluteProfit = profitCalculator.calculateNetAbsoluteProfit(transaction.getNumber(), depotEntry.getSinglePrice(),
            transaction.getPrice());
        BigDecimal netPercentageProfit = profitCalculator.calculatePercentageProfit(netAbsoluteProfit, transaction.getNumber(), depotEntry.getSinglePrice());
        BigDecimal grossAbsoluteProfit = profitCalculator.calculateGrossAbsoluteProfit(netAbsoluteProfit, transaction.getNumber(),
            depotEntry.getCosts().divide(depotEntry.getNumber(), 6, RoundingMode.HALF_UP), transaction.getExpenses());
        BigDecimal grossPercentageProfit = profitCalculator.calculatePercentageProfit(grossAbsoluteProfit, transaction.getNumber(), depotEntry.getSinglePrice());

        profit = new Profit(transaction.getUserId(), transaction.getDepotName(), transaction.getIsin(), transaction.getSecurityName(), netAbsoluteProfit, grossAbsoluteProfit,
            netPercentageProfit, grossPercentageProfit);
    }

    private void updateProfit(Transaction transaction, DepotEntry depotEntry, Profit profit) {
        BigDecimal newNetAbsoluteProfit = profitCalculator.calculateNetAbsoluteProfit(transaction.getNumber(), depotEntry.getSinglePrice(), transaction.getPrice());
        BigDecimal totalNetAbsoluteProfit = newNetAbsoluteProfit.add(profit.getNetAbsoluteProfit());
        BigDecimal netPercentageProfit = profitCalculator.calculatePercentageProfit(totalNetAbsoluteProfit, profit.getNetAbsoluteProfit(),
            profit.getNetPercentageProfit(), transaction.getNumber(), depotEntry.getSinglePrice());
        BigDecimal grossAbsoluteProfit = profitCalculator.calculateGrossAbsoluteProfit(newNetAbsoluteProfit, transaction.getNumber(),
            depotEntry.getCosts().divide(depotEntry.getNumber(), 6, RoundingMode.HALF_UP), transaction.getExpenses(), profit.getGrossAbsoluteProfit());
        BigDecimal grossPercentageProfit = profitCalculator.calculatePercentageProfit(grossAbsoluteProfit, profit.getNetAbsoluteProfit(), profit.getNetPercentageProfit(),
            transaction.getNumber(), depotEntry.getSinglePrice());

        this.profit = new Profit(profit.getProfitId(), transaction.getUserId(), transaction.getDepotName(), transaction.getIsin(), transaction.getSecurityName(), totalNetAbsoluteProfit, grossAbsoluteProfit,
            netPercentageProfit, grossPercentageProfit);
//        repository.save(profit);
    }
}
