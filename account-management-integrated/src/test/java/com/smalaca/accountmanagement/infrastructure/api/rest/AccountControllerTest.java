package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeRequestDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.TransferDto;
import com.smalaca.infrastructure.api.rest.contract.ExchangeContract;
import com.smalaca.infrastructure.api.rest.contract.Scenario;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountControllerTest {
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AccountFactory accountFactory = mock(AccountFactory.class);
    private final AccountController accountController = new AccountController(accountRepository, customerRepository, accountFactory, restTemplate);
    private final ExchangeContract exchangeContract = new ExchangeContract();

    @Test
    void shouldTransferMoneyFromEurAccountToPlnAccount() {
        ExchangeRequestDto request = givenExchangeFor(exchangeContract.eurToPln());
        Account from = existingAccount(request.getFrom());
        Account to = existingAccount(request.getTo());
        TransferDto transferDto = transferDto(from, to, request.getAmount(), request.getDate());

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(900));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(1400));
    }

    @Test
    void shouldTransferMoneyFromPlnAccountToEurAccount() {
        ExchangeRequestDto request = givenExchangeFor(exchangeContract.plnToUsd());
        Account from = existingAccount(request.getFrom());
        Account to = existingAccount(request.getTo());
        TransferDto transferDto = transferDto(from, to, request.getAmount(), request.getDate());

        String actual = accountController.transfer(transferDto);

        assertThat(actual).isEqualTo("SUCCESS");
        assertThat(from.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(to.asDto().getBalance()).isEqualTo(BigDecimal.valueOf(2800));
    }

    @Test
    void shouldTransferMoneyFromAccountWithUnknownCurrency() {
        ExchangeRequestDto request = givenExchangeFor(exchangeContract.unknownCurrency());
        Account from = existingAccount(request.getFrom());
        Account to = existingAccount(request.getTo());
        TransferDto transferDto = transferDto(from, to, request.getAmount(), request.getDate());

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

    private ExchangeRequestDto givenExchangeFor(Scenario scenario) {
        ExchangeRequestDto request = scenario.getRequest();
        ExchangeResponseDto response = scenario.getResponse();
        ResponseEntity<ExchangeResponseDto> responseEntity = mock(ResponseEntity.class);
        given(responseEntity.getBody()).willReturn(response);
        given(restTemplate.postForEntity("http://localhost:8001/currency/", request, ExchangeResponseDto.class)).willReturn(responseEntity);
        return request;
    }
}