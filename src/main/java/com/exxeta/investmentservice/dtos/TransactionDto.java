package com.exxeta.investmentservice.dtos;

import com.exxeta.investmentservice.util.LocalDateDeserializer;
import com.exxeta.investmentservice.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

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

    private String isin;

    private String securityName;

    @NotNull
    private BigDecimal number;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal expenses;
    @NotNull
    private BigDecimal totalPrice;

    public TransactionDto() {
    }

    public TransactionDto(long userId, @NotNull LocalDate date, @NotNull String depotName, @NotNull String type, String isin, String securityName, @NotNull BigDecimal number, @NotNull BigDecimal price, @NotNull BigDecimal expenses, @NotNull BigDecimal totalPrice) {
        this.userId = userId;
        this.date = date;
        this.depotName = depotName;
        this.type = type;
        this.isin = isin;
        this.securityName = securityName;
        this.number = number;
        this.price = price;
        this.expenses = expenses;
        this.totalPrice = totalPrice;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDepotName() {
        return depotName;
    }

    public String getType() {
        return type;
    }

    public String getIsin() {
        return isin;
    }

    public String getSecurityName() {
        return securityName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
