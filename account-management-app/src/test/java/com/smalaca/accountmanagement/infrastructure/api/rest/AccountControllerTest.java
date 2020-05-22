package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.TransferDto;
import com.smalaca.cdc.contract.ContractReader;
import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.smalaca.accountmanagement.domain.currency.Currency.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountControllerTest {
    private static final String SOME_DATE = "2020-01-01";

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AccountFactory accountFactory = mock(AccountFactory.class);
    private final AccountController accountController = new AccountController(accountRepository, customerRepository, accountFactory);

    @Test
    void shouldTransferMoneyFromEurAccountToPlnAccount() {
        Account from = existingAccount(EUR.name());
        Account to = existingAccount(PLN.name());
        TransferDto transferDto = transferDto(from, to, 100);

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(900));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1100));
    }

    @Test
    void shouldTransferMoneyFromPlnAccountToEurAccount() {
        Account from = existingAccount(PLN.name());
        Account to = existingAccount(USD.name());
        TransferDto transferDto = transferDto(from, to, 900);

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1900));
    }

    @Test
    void shouldTransferMoneyFromAccountWithUnknownCurrency() {
        Account from = existingAccount(RUB.name());
        Account to = existingAccount(USD.name());
        TransferDto transferDto = transferDto(from, to, 200);

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(800));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1200));
    }

    private TransferDto transferDto(Account from, Account to, long amount) {
        return new TransferDto(from.accountNumber(), to.accountNumber(), BigDecimal.valueOf(amount), SOME_DATE);
    }

    private Account existingAccount(String currency) {
        Account from = new AccountFactory().createFor(new Customer(login()), currency);
        from.deposit(BigDecimal.valueOf(1000));
        given(accountRepository.findByNumber(from.accountNumber())).willReturn(Optional.of(from));
        return from;
    }

    private String login() {
        return UUID.randomUUID().toString();
    }
}