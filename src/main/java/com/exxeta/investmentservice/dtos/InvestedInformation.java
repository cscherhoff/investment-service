package com.exxeta.investmentservice.dtos;

import java.math.BigDecimal;

public class InvestedInformation {

    private BigDecimal dedicated;
    private BigDecimal invested;
    private BigDecimal investable;

    public InvestedInformation(BigDecimal dedicated, BigDecimal invested, BigDecimal investable) {
        this.dedicated = dedicated;
        this.invested = invested;
        this.investable = investable;
    }

    public BigDecimal getDedicated() {
        return dedicated;
    }

    public BigDecimal getInvested() {
        return invested;
    }

    public BigDecimal getInvestable() {
        return investable;
    }
}
