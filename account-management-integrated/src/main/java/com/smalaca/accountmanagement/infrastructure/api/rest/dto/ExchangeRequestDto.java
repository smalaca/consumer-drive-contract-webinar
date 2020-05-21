package com.smalaca.accountmanagement.infrastructure.api.rest.dto;

public class ExchangeRequestDto {
    private String from;
    private String to;
    private long amount;
    private String date;

    public ExchangeRequestDto() {}

    public ExchangeRequestDto(String from, String to, long amount, String date) {
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

    public long getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRequestDto exchangeRequestDto = (ExchangeRequestDto) o;

        if (amount != exchangeRequestDto.amount) return false;
        if (from != null ? !from.equals(exchangeRequestDto.from) : exchangeRequestDto.from != null) return false;
        if (to != null ? !to.equals(exchangeRequestDto.to) : exchangeRequestDto.to != null) return false;
        return date != null ? date.equals(exchangeRequestDto.date) : exchangeRequestDto.date == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
