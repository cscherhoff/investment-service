package com.exxeta.investmentservice.service.profits;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProfitCalculator {

    protected BigDecimal calculateNetAbsoluteProfit(BigDecimal numberSold, BigDecimal averagePriceForBuying, BigDecimal priceForSelling) {
        return numberSold.multiply(priceForSelling.subtract(averagePriceForBuying)).setScale(4, RoundingMode.HALF_UP);
    }

    protected BigDecimal calculateGrossAbsoluteProfit(BigDecimal netAbsoluteProfit, BigDecimal numberSold,
        BigDecimal averageCostsForBuying, BigDecimal costForSelling) {
        return netAbsoluteProfit.subtract(numberSold.multiply(averageCostsForBuying)).subtract(costForSelling)
            .setScale(4, RoundingMode.HALF_UP);
    }

    protected BigDecimal calculateGrossAbsoluteProfit(BigDecimal netAbsoluteProfit, BigDecimal numberSold, BigDecimal averageCostsForBuying,
        BigDecimal costForSelling, BigDecimal grossAbsoluteProfitFromEarlierProfits) {
        return grossAbsoluteProfitFromEarlierProfits.add(calculateGrossAbsoluteProfit(netAbsoluteProfit, numberSold,
            averageCostsForBuying, costForSelling)).setScale(8, RoundingMode.HALF_UP);
    }

    protected BigDecimal calculatePercentageProfit(BigDecimal absoluteProfit, BigDecimal numberSold, BigDecimal averagePriceForBuying) {
        return absoluteProfit.divide(numberSold.multiply(averagePriceForBuying), 10, RoundingMode.HALF_UP);
    }

    protected BigDecimal calculatePercentageProfit(BigDecimal absoluteProfit, BigDecimal netAbsoluteProfitFromEarlierProfits,
        BigDecimal netPercentageProfitFromEarlierProfits, BigDecimal numberSold, BigDecimal averagePriceForBuying) {
        BigDecimal boughtFromEarlier = netAbsoluteProfitFromEarlierProfits
            .divide(netPercentageProfitFromEarlierProfits, 10, RoundingMode.HALF_UP);
        BigDecimal boughtFromNewTransaction = numberSold.multiply(averagePriceForBuying);
        BigDecimal boughtTotal = boughtFromEarlier.add(boughtFromNewTransaction);
        return absoluteProfit.divide(boughtTotal, 10, RoundingMode.HALF_UP);
    }
}
