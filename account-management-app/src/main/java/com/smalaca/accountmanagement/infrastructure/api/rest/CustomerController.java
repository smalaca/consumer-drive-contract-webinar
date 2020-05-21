package com.smalaca.accountmanagement.infrastructure.api.rest;

import com.smalaca.accountmanagement.domain.customer.Customer;
import com.smalaca.accountmanagement.domain.customer.CustomerRepository;
import com.smalaca.accountmanagement.infrastructure.api.rest.exception.CustomerNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{login}")
    public Long getId(@PathVariable String login) {
        Optional<Customer> customer = customerRepository.findByLogin(login);

        if (customer.isPresent()) {
            return customer.get().id();
        }

        throw new CustomerNotFoundException(login);
    }
}
