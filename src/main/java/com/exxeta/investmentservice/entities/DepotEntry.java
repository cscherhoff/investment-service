package com.exxeta.investmentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
public class DepotEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private long depotEntryId;

    @JsonIgnore
    private long userId;
    private String depotName;
    private String securityName;
    private BigDecimal number;
    private BigDecimal singlePrice;
    private BigDecimal dividend = BigDecimal.ZERO;
    private BigDecimal costs = BigDecimal.ZERO;

    public DepotEntry() {
    }

    public DepotEntry(String securityName, BigDecimal number, BigDecimal singlePrice, BigDecimal dividend,
        BigDecimal costs) {
        this.securityName = securityName;
        this.number = number;
        this.singlePrice = singlePrice;
        this.dividend = dividend;
        this.costs = costs;
    }

    public DepotEntry(String depotName, String securityName, BigDecimal number, BigDecimal singlePrice, BigDecimal costs) {
        this.depotName = depotName;
        this.securityName = securityName;
        this.number = number;
        this.singlePrice = singlePrice;
        this.costs = costs;
    }

    public DepotEntry(long userId, String depotName, String securityName, BigDecimal number,
        BigDecimal singlePrice, BigDecimal costs) {
        this.userId = userId;
        this.depotName = depotName;
        this.securityName = securityName;
        this.number = number;
        this.singlePrice = singlePrice;
        this.costs = costs;
    }

    public void updateNumber(BigDecimal number) {
        this.number = this.number.add(number);
    }

    public void updateCosts(BigDecimal costs) {
        updateCosts(costs, BigDecimal.ZERO);
    }

    public void updateCosts(BigDecimal costs, BigDecimal number) {
        if (number.doubleValue()==0.0) {
            this.costs = this.costs.add(costs);
        } else {
            BigDecimal costsPerSecurity = this.costs.divide(this.number, 6, RoundingMode.HALF_UP);
            this.costs = this.costs.subtract(number.multiply(costsPerSecurity));
        }
    }

    public void updatePrice(BigDecimal number, BigDecimal price) {
        this.singlePrice = calculatePrice(number, price, this.number, this.singlePrice);
    }

    private BigDecimal calculatePrice(BigDecimal transactionNumber, BigDecimal transactionPrice, BigDecimal depotNumber,
        BigDecimal depotPrice) {
        BigDecimal totalSumOfPriceFromDepot = depotNumber.multiply(depotPrice);
        BigDecimal totalSumOfPriceFromTransaction = transactionNumber.multiply(transactionPrice);
        BigDecimal sumOfPrices = totalSumOfPriceFromDepot.add(totalSumOfPriceFromTransaction);
        BigDecimal sumOfNumbers = transactionNumber.add(depotNumber);
        return sumOfPrices.divide(sumOfNumbers, 9, RoundingMode.HALF_UP);
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSecurityName() {
        return securityName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public BigDecimal getCosts() {
        return costs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DepotEntry that = (DepotEntry) o;
        return Objects.equals(depotName, that.depotName) &&
            Objects.equals(securityName, that.securityName) &&
            Objects.equals(number, that.number) &&
            Objects.equals(singlePrice, that.singlePrice) &&
            Objects.equals(dividend, that.dividend) &&
            Objects.equals(costs, that.costs);
    }
}
