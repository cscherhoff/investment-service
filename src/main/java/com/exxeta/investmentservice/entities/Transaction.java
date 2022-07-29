package com.exxeta.investmentservice.entities;

import com.exxeta.investmentservice.util.LocalDateDeserializer;
import com.exxeta.investmentservice.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @JsonIgnore
    private long transactionId;

    @JsonIgnore
    private long userId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull
    private LocalDate date;
    @NotNull
    private String depotName;
    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "security")
    private Security security;

    @NotNull
    private BigDecimal number;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal expenses;
    @NotNull
    private BigDecimal totalPrice;

    public Transaction() {

    }

    public Transaction(String depotName, LocalDate date, String type, String isin,
        BigDecimal price, BigDecimal number, BigDecimal expenses, BigDecimal totalPrice) {
        this.depotName = depotName;
        this.date = date;
        this.type = type;
        this.security = new Security(isin, "placeHolderName");
        this.price = price;
        this.number = number;
        this.expenses = expenses;
        this.totalPrice = totalPrice;
    }

    public Transaction(String depotName, LocalDate date, String type, String isin, String securityName,
                       BigDecimal price, BigDecimal number, BigDecimal expenses, BigDecimal totalPrice) {
        this.depotName = depotName;
        this.date = date;
        this.type = type;
        this.security = new Security(isin, securityName);
        this.price = price;
        this.number = number;
        this.expenses = expenses;
        this.totalPrice = totalPrice;
    }

    public Transaction(long userId, @NotNull LocalDate date, @NotNull String depotName, @NotNull String type, Security security, @NotNull BigDecimal number, @NotNull BigDecimal price, @NotNull BigDecimal expenses, @NotNull BigDecimal totalPrice) {
        this.userId = userId;
        this.date = date;
        this.depotName = depotName;
        this.type = type;
        this.security = security;
        this.number = number;
        this.price = price;
        this.expenses = expenses;
        this.totalPrice = totalPrice;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDepotName() {
        return depotName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Security getSecurity() {
        return security;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getIsin() {
        return security.getIsin();
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "transactionId=" + transactionId +
            ", userId=" + userId +
            ", depotName='" + depotName + '\'' +
            ", date=" + date +
            ", type='" + type + '\'' +
            ", securityName='" + security.getSecurityName() + '\'' +
            ", price=" + price +
            ", number=" + number +
            ", expenses=" + expenses +
            ", totalAmount=" + totalPrice +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return getUserId() == that.getUserId() &&
            Objects.equals(getDepotName(), that.getDepotName()) &&
            Objects.equals(getDate(), that.getDate()) &&
            Objects.equals(getType(), that.getType()) &&
            Objects.equals(getIsin(), that.getIsin()) &&
            Objects.equals(getPrice(), that.getPrice()) &&
            Objects.equals(getNumber(), that.getNumber()) &&
            Objects.equals(getExpenses(), that.getExpenses()) &&
            Objects.equals(getTotalPrice(), that.getTotalPrice());
    }
}
