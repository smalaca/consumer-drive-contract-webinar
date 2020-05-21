package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountDto;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeRequestDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.NewAccountDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.TransferDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.exception.CustomerNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfer")
public class AccountController {
    private static final String CURRENCIES_EXCHANGE = "http://localhost:8001/currency/";

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountFactory accountFactory;
    private final RestTemplate restTemplate;

    public AccountController(AccountRepository accountRepository, CustomerRepository customerRepository, AccountFactory accountFactory, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountFactory = accountFactory;
        this.restTemplate = restTemplate;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Long createAccount(@RequestBody NewAccountDto newAccountDto) {
        Optional<Customer> customer = customerRepository.findById(newAccountDto.getId());

        if (customer.isPresent()) {
            Account account = accountFactory.createFor(customer.get(), newAccountDto.getCurrency());
            accountRepository.save(account);

            return account.id();
        }

        throw new CustomerNotFoundException(newAccountDto.getId());
    }

    @PutMapping("/transfer")
    public String transfer(@RequestBody TransferDto transferDto) {
        Optional<Account> from = accountRepository.findByNumber(transferDto.getFrom());
        Optional<Account> to = accountRepository.findByNumber(transferDto.getTo());

        if (from.isPresent() && to.isPresent()) {
            ExchangeResponseDto exchange = exchange(from.get(), to.get(), transferDto.getAmount(), transferDto.getDate());

            if (exchange.getError() == null) {
                transfer(from.get(), to.get(), transferDto.getAmount(), deposit(exchange));
                return "SUCCESS";
            }
        }

        return "FAILED";
    }

    private void transfer(Account from, Account to, BigDecimal withdrawal, BigDecimal deposit) {
        from.withdraw(withdrawal);
        to.deposit(deposit);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    private BigDecimal deposit(ExchangeResponseDto exchange) {
        return BigDecimal.valueOf(exchange.getAmount());
    }

    private ExchangeResponseDto exchange(Account from, Account to, BigDecimal amount, String date) {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(from.asDto().getCurrency(), to.asDto().getCurrency(), amount.longValue(), date);
        return exchange(exchangeRequestDto);
    }

    private ExchangeResponseDto exchange(ExchangeRequestDto request) {
        return restTemplate.postForEntity(CURRENCIES_EXCHANGE, request, ExchangeResponseDto.class).getBody();
    }

    @GetMapping("/{ownerId}")
    public List<AccountDto> accountNumbers(@PathVariable Long ownerId) {
        return accountRepository.findByOwnerId(ownerId).stream()
            .map(Account::asDto)
            .collect(Collectors.toList());
    }
}
