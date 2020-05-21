package com.smalaca.accountmanagement.infrastructure.init;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.currency.Currency;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.smalaca.accountmanagement.domain.currency.Currency.*;

@Component
public class ExistingCustomersLoader {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    public ExistingCustomersLoader(CustomerRepository customerRepository, AccountRepository accountRepository, AccountFactory accountFactory) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
    }

    @Async
    @EventListener
    private void process(ContextRefreshedEvent event) {
        Customer parker = customerRepository.save(new Customer("parker"));
        createAccount(parker, PLN, 1000);

        Customer stark = customerRepository.save(new Customer("stark"));
        createAccount(stark, PLN, 1000);
        createAccount(stark, USD, 1000);
        createAccount(stark, EUR, 1000);

        Customer banner = customerRepository.save(new Customer("banner"));
        createAccount(banner, USD, 1000);
        createAccount(banner, EUR, 1000);
    }

    private void createAccount(Customer parker, Currency currency, int balance) {
        Account account = accountFactory.createFor(parker, currency.name());
        account.deposit(BigDecimal.valueOf(balance));

        accountRepository.save(account);
    }
}
