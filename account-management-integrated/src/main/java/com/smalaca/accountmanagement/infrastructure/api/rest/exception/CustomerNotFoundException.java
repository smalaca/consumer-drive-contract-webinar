package com.smalaca.accountmanagement.infrastructure.api.rest.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer with id: " + id + " not found.");
    }

    public CustomerNotFoundException(String login) {
        super("Customer with login: " + login + " not found.");
    }
}
