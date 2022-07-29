package com.exxeta.investmentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Profit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private long profitId;

    @JsonIgnore
    private long userId;

    @JsonIgnore
    private String depotName;

    private String isin;
    private String securityName;
    private BigDecimal netAbsoluteProfit;
    private BigDecimal grossAbsoluteProfit;
    private BigDecimal netPercentageProfit;
    private BigDecimal grossPercentageProfit;

    public Profit() {

    }

//    public Profit(String securityName, BigDecimal netAbsoluteProfit, BigDecimal grossAbsoluteProfit,
//        BigDecimal netPercentageProfit, BigDecimal grossPercentageProfit) {
//        this.securityName = securityName;
//        this.netAbsoluteProfit = netAbsoluteProfit;
//        this.grossAbsoluteProfit = grossAbsoluteProfit;
//        this.netPercentageProfit = netPercentageProfit;
//        this.grossPercentageProfit = grossPercentageProfit;
//    }

    public Profit(long userId, String depotName, String isin, BigDecimal netAbsoluteProfit,
        BigDecimal grossAbsoluteProfit, BigDecimal netPercentageProfit, BigDecimal grossPercentageProfit) {
        this.userId = userId;
        this.depotName = depotName;
        this.isin = isin;
        this.netAbsoluteProfit = netAbsoluteProfit;
        this.grossAbsoluteProfit = grossAbsoluteProfit;
        this.netPercentageProfit = netPercentageProfit;
        this.grossPercentageProfit = grossPercentageProfit;
    }

    public Profit(long userId, String depotName, String isin, String securityName, BigDecimal netAbsoluteProfit,
                  BigDecimal grossAbsoluteProfit, BigDecimal netPercentageProfit, BigDecimal grossPercentageProfit) {
        this.userId = userId;
        this.depotName = depotName;
        this.isin = isin;
        this.securityName = securityName;
        this.netAbsoluteProfit = netAbsoluteProfit;
        this.grossAbsoluteProfit = grossAbsoluteProfit;
        this.netPercentageProfit = netPercentageProfit;
        this.grossPercentageProfit = grossPercentageProfit;
    }

    public Profit(long profitId, long userId, String depotName, String isin, String securityName, BigDecimal netAbsoluteProfit,
        BigDecimal grossAbsoluteProfit, BigDecimal netPercentageProfit, BigDecimal grossPercentageProfit) {
        this.profitId = profitId;
        this.userId = userId;
        this.depotName = depotName;
        this.isin = isin;
        this.securityName = securityName;
        this.netAbsoluteProfit = netAbsoluteProfit;
        this.grossAbsoluteProfit = grossAbsoluteProfit;
        this.netPercentageProfit = netPercentageProfit;
        this.grossPercentageProfit = grossPercentageProfit;
    }

    public long getProfitId() {
        return profitId;
    }

    public String getIsin() {
        return isin;
    }

    public String getSecurityName() {
        return securityName;
    }

    public BigDecimal getNetAbsoluteProfit() {
        return netAbsoluteProfit;
    }

    public BigDecimal getGrossAbsoluteProfit() {
        return grossAbsoluteProfit;
    }

    public BigDecimal getNetPercentageProfit() {
        return netPercentageProfit;
    }

    public BigDecimal getGrossPercentageProfit() {
        return grossPercentageProfit;
    }

    public String getDepotName() {
        return depotName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profit profit = (Profit) o;
        return Objects.equals(depotName, profit.depotName) &&
            Objects.equals(isin, profit.isin) &&
            Objects.equals(netAbsoluteProfit, profit.netAbsoluteProfit) &&
            Objects.equals(grossAbsoluteProfit, profit.grossAbsoluteProfit) &&
            Objects.equals(netPercentageProfit, profit.netPercentageProfit) &&
            Objects.equals(grossPercentageProfit, profit.grossPercentageProfit);
    }
}
