package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.TransferDto;
import com.smalaca.accountmanagement.infrastructure.currencyapp.CurrencyApplicationClient;
import com.smalaca.cdc.contract.ContractReader;
import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountControllerTest {
    private static final int EUR_TO_PLN = 0;
    private static final int PLN_TO_USD = 1;
    private static final int UNKNOWN_CURRENCY = 2;

    private final CurrencyApplicationClient currencyApplicationClient = mock(CurrencyApplicationClient.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AccountFactory accountFactory = mock(AccountFactory.class);
    private final AccountController accountController = new AccountController(accountRepository, customerRepository, accountFactory);
    private final ContractReader contractReader = new ContractReader();

    @Test
    void shouldTransferMoneyFromEurAccountToPlnAccount() {
        Input input = givenExchangeFor(EUR_TO_PLN);
        Account from = existingAccount(input.getFrom());
        Account to = existingAccount(input.getTo());
        TransferDto transferDto = transferDto(from, to, input.getAmount(), input.getDate());

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(900));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1400));
    }

    @Test
    void shouldTransferMoneyFromPlnAccountToEurAccount() {
        Input input = givenExchangeFor(PLN_TO_USD);
        Account from = existingAccount(input.getFrom());
        Account to = existingAccount(input.getTo());
        TransferDto transferDto = transferDto(from, to, input.getAmount(), input.getDate());

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(2800));
    }

    @Test
    void shouldTransferMoneyFromAccountWithUnknownCurrency() {
        Input input = givenExchangeFor(UNKNOWN_CURRENCY);
        Account from = existingAccount(input.getFrom());
        Account to = existingAccount(input.getTo());
        TransferDto transferDto = transferDto(from, to, input.getAmount(), input.getDate());

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("FAILED");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1000));
    }

    private TransferDto transferDto(Account from, Account to, long amount, String date) {
        return new TransferDto(from.accountNumber(), to.accountNumber(), BigDecimal.valueOf(amount), date);
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

    private Input givenExchangeFor(int scenario) {
        Input input = contractReader.inputForScenario(scenario);
        Output output = contractReader.outputForScenario(scenario);
        given(currencyApplicationClient.exchange(input)).willReturn(output);
        return input;
    }
}