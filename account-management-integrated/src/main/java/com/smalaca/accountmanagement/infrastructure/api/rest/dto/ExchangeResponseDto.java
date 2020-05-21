package com.smalaca.accountmanagement.infrastructure.api.rest.dto;

public class ExchangeResponseDto {
    private long amount;
    private String error;

    public ExchangeResponseDto() {}

    public ExchangeResponseDto(long amount, String error) {
        this.amount = amount;
        this.error = error;
    }

    public long getAmount() {
        return amount;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeResponseDto exchangeResponseDto = (ExchangeResponseDto) o;

        if (amount != exchangeResponseDto.amount) return false;
        return error != null ? error.equals(exchangeResponseDto.error) : exchangeResponseDto.error == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (amount ^ (amount >>> 32));
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }
}
