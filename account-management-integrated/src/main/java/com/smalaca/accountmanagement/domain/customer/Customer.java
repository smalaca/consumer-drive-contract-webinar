package com.smalaca.accountmanagement.domain.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String login;

    private Customer() {}

    public Customer(String login) {
        this.login = login;
    }

    public Long id() {
        return id;
    }
}
