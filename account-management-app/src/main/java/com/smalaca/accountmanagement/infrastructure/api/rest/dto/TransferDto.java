package com.smalaca.accountmanagement.infrastructure.api.rest.dto;

import java.math.BigDecimal;

public class TransferDto {
    private String from;
    private String to;
    private BigDecimal amount;
    private String date;

    public TransferDto() {}

    public TransferDto(String from, String to, BigDecimal amount, String date) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
