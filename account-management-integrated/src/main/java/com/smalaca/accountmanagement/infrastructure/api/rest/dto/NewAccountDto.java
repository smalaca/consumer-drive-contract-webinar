package com.smalaca.accountmanagement.infrastructure.api.rest.dto;

public class NewAccountDto {
    private Long id;
    private String currency;

    public NewAccountDto() {}

    public NewAccountDto(Long id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }
}
