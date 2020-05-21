package com.smalaca.accountmanagement.domain.account;

import com.smalaca.accountmanagement.domain.currency.Currency;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String number;

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    private Account() {}

    private Account(String number, Long ownerId, Currency currency, BigDecimal balance) {
        this.number = number;
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = balance;
    }

    public static Account newAccount(String number, Long ownerId, Currency currency) {
        return new Account(number, ownerId, currency, BigDecimal.ZERO);
    }

    public Long id() {
        return id;
    }

    public String accountNumber() {
        return number;
    }

    public void deposit(BigDecimal deposit) {
        balance = balance.add(deposit);
    }

    public void withdraw(BigDecimal deposit) {
        balance = balance.subtract(deposit);
    }

    public AccountDto asDto() {
        return new AccountDto(number, ownerId, currency.name(), balance);
    }
}
