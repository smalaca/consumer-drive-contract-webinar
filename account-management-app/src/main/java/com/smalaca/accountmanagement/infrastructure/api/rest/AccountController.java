package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.account.Account;
import com.smalaca.accountmanagement.domain.account.AccountDto;
import com.smalaca.accountmanagement.domain.account.AccountFactory;
import com.smalaca.accountmanagement.domain.account.AccountRepository;
import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.NewAccountDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.TransferDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.exception.CustomerNotFoundException;
import com.smalaca.accountmanagement.infrastructure.currencyapp.CurrencyApplicationClient;
import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountFactory accountFactory;
    private final CurrencyApplicationClient currencyApplicationClient;

    public AccountController(AccountRepository accountRepository, CustomerRepository customerRepository, AccountFactory accountFactory, CurrencyApplicationClient currencyApplicationClient) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountFactory = accountFactory;
        this.currencyApplicationClient = currencyApplicationClient;
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
            Output exchange = exchange(from.get(), to.get(), transferDto.getAmount(), transferDto.getDate());

            if (exchange.getError() == null) {
                transfer(from.get(), to.get(), transferDto.getAmount(), deposit(exchange));
                return "SUCCESS";
            }
        }

        return "FAILED";
    }

    private void transfer(Account from, Account to, BigDecimal amount, BigDecimal deposit) {
        from.withdraw(amount);
        to.deposit(deposit);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    private BigDecimal deposit(Output exchange) {
        return BigDecimal.valueOf(exchange.getAmount());
    }

    private Output exchange(Account from, Account to, BigDecimal amount, String date) {
        Input input = new Input(from.asDto().getCurrency(), to.asDto().getCurrency(), amount.longValue(), date);
        return currencyApplicationClient.exchange(input);
    }

    @GetMapping("/{ownerId}")
    public List<AccountDto> accountNumbers(@PathVariable Long ownerId) {
        return accountRepository.findByOwnerId(ownerId).stream()
            .map(Account::asDto)
            .collect(Collectors.toList());
    }
}
