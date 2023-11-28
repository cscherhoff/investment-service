package com.exxeta.investmentservice.dtos;

import java.math.BigDecimal;

public class TransferDto {

    private String userId;
    private String accountName;
    private BigDecimal amount;

    public TransferDto() {
    }

    public TransferDto(String userId, String accountName, BigDecimal amount) {
        this.userId = userId;
        this.accountName = accountName;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
