package com.smalaca.accountmanagement.domain.account;

import java.math.BigDecimal;

public class AccountDto {
    private final String number;
    private final Long ownerId;
    private final String currency;
    private final BigDecimal balance;

    AccountDto(String number, Long ownerId, String currency, BigDecimal balance) {
        this.number = number;
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
