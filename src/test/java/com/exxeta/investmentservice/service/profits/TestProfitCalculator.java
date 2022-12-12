package com.exxeta.investmentservice.service.profits;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProfitCalculator {

    private final ProfitCalculator profitCalculator = new ProfitCalculator();

    @Test
    public void testCalculateNetProfitAbsAsFirstProfit() {
        BigDecimal numberSold = BigDecimal.valueOf(3);
        BigDecimal priceBought = BigDecimal.valueOf(16.24354);
        BigDecimal priceSell = BigDecimal.valueOf(24.67);

        BigDecimal expected = BigDecimal.valueOf(25.2794);
        BigDecimal actual = profitCalculator.calculateNetAbsoluteProfit(numberSold, priceBought, priceSell);
        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The net absolute profits have to be the same!");
    }

    @Test
    public void testCalculateNetProfitPercentageAsFirstProfit() {
        BigDecimal numberSold = BigDecimal.valueOf(3);
        BigDecimal priceForBuying = BigDecimal.valueOf(16.24354);
        BigDecimal netProfitAbsolute = BigDecimal.valueOf(25.2794);

        BigDecimal expected = BigDecimal.valueOf(0.5187580211);
        BigDecimal actual = profitCalculator.calculatePercentageProfit(netProfitAbsolute, numberSold, priceForBuying);

        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The net percentage profits have to be the same!");
    }

    @Test
    public void testCalculateGrossProfitAbsoluteAsFirstProfit() {
        BigDecimal numberSold = BigDecimal.valueOf(3);
        BigDecimal averageCostsForBuying = BigDecimal.valueOf(0.076923);
        BigDecimal costsForSelling = BigDecimal.valueOf(1);
        BigDecimal netAbsoluteProfit = BigDecimal.valueOf(25.2794);

        BigDecimal expected = BigDecimal.valueOf(24.0486);
        BigDecimal actual = profitCalculator
            .calculateGrossAbsoluteProfit(netAbsoluteProfit, numberSold, averageCostsForBuying,
            costsForSelling);

        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The gross absolute profits have to be the same!");
    }

    @Test
    public void testCalculateGrossProfitProzAsFirstProfit() {

        BigDecimal numberSold = BigDecimal.valueOf(3);
        BigDecimal priceForBuying = BigDecimal.valueOf(16.2435);
        BigDecimal grossAbsoluteProfit = BigDecimal.valueOf(24.0486);

        BigDecimal expected = BigDecimal.valueOf(0.4935020162);

        BigDecimal actual = profitCalculator.calculatePercentageProfit(grossAbsoluteProfit, numberSold, priceForBuying);
        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The gross percentage profits have to be the same!");
    }

    @Test
    public void testCalculateNetProfitAbsoluteWithOtherProfits() {
        BigDecimal numberSold = BigDecimal.valueOf(12);
        BigDecimal priceForBuying = BigDecimal.valueOf(16.24353846);
        BigDecimal priceForSelling = BigDecimal.valueOf(25.82);
        BigDecimal absoluteNetProfitFromEarlierProfits = BigDecimal.valueOf(25.2794);

        BigDecimal expected = BigDecimal.valueOf(140.1969);
        BigDecimal actual = profitCalculator
            .calculateNetAbsoluteProfit(numberSold, priceForBuying, priceForSelling).add(absoluteNetProfitFromEarlierProfits);

        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The net absolute profits have to be the same!");
    }

    @Test
    public void testCalculateNetProfitPercentageWithOtherProfits() {
        BigDecimal totalNetAbsoluteProfit = BigDecimal.valueOf(140.1969);
        BigDecimal numberSold = BigDecimal.valueOf(12);
        BigDecimal averagePriceForBuying = BigDecimal.valueOf(16.24353846);
        BigDecimal netAbsoluteProfitFromEarlierProfits = BigDecimal.valueOf(25.2794);
        BigDecimal netPercentageProfitFromEarlierProfits = BigDecimal.valueOf(0.5188);

        BigDecimal expected = BigDecimal.valueOf(0.5754048665);
        BigDecimal actual = profitCalculator.calculatePercentageProfit(totalNetAbsoluteProfit,
            netAbsoluteProfitFromEarlierProfits, netPercentageProfitFromEarlierProfits, numberSold, averagePriceForBuying);

        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The net percentage profits have to be the same!");
    }

    @Test
    public void testCalculateGrossProfitAbsoluteWithOtherProfits() {
        BigDecimal netAbsoluteProfit = BigDecimal.valueOf(114.9175);
        BigDecimal numberSold = BigDecimal.valueOf(12);
        BigDecimal averageCostsForBuying = BigDecimal.valueOf(0.076923);
        BigDecimal costsForSelling = BigDecimal.valueOf(1);
        BigDecimal grossAbsoluteProfitFromEarlierProfits = BigDecimal.valueOf(24.0486);

        BigDecimal expected = BigDecimal.valueOf(137.043);
        BigDecimal actual = profitCalculator
            .calculateGrossAbsoluteProfit(netAbsoluteProfit, numberSold, averageCostsForBuying,
            costsForSelling, grossAbsoluteProfitFromEarlierProfits);

        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The gross absolute profits have to be the same!");
    }

    @Test
    public void testCalculateGrossProfitPercentageWithOtherProfits() {
        BigDecimal absoluteProfit = BigDecimal.valueOf(137.043077);
        BigDecimal netAbsoluteProfitFromEarlierProfits = BigDecimal.valueOf(25.2793846);
        BigDecimal netPercentageProfitFromEarlierProfits = BigDecimal.valueOf(0.51875775);
        BigDecimal numberSold = BigDecimal.valueOf(12);
        BigDecimal averagePriceBought = BigDecimal.valueOf(16.24);

        BigDecimal actual = profitCalculator
            .calculatePercentageProfit(absoluteProfit, netAbsoluteProfitFromEarlierProfits,
            netPercentageProfitFromEarlierProfits, numberSold, averagePriceBought);

        BigDecimal expected = BigDecimal.valueOf(0.5625496925);
        assertEquals(expected.doubleValue(), actual.doubleValue(), 0.0, "The gross percentage profits have to be the same!");
    }
}
