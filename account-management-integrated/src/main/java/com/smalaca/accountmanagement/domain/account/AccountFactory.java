package com.smalaca.accountmanagement.domain.account;

import com.smalaca.accountmanagement.domain.currency.Currency;
import com.smalaca.accountmanagement.domain.customer.Customer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountFactory {
    public Account createFor(Customer customer, String currencyString) {
        Currency currency = Currency.valueOf(currencyString);
        return Account.newAccount(accountNumber(), customer.id(), currency);
    }

    private String accountNumber() {
        return UUID.randomUUID().toString();
    }
}
