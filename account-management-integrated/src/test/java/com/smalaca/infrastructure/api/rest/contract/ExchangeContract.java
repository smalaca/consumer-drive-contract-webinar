package com.smalaca.infrastructure.api.rest.contract;

import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeRequestDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;

public class ExchangeContract {
    public Scenario eurToPln() {
        return new Scenario(new ExchangeRequestDto("EUR", "PLN", 100, "2019-12-24"), new ExchangeResponseDto(400, null));
    }
    public Scenario plnToUsd() {
        return new Scenario(new ExchangeRequestDto("PLN", "USD", 900, "2020-01-28"), new ExchangeResponseDto(1800, null));
    }

    public Scenario unknownCurrency() {
        return new Scenario(new ExchangeRequestDto("RUB", "USD", 900, "2020-01-28"), new ExchangeResponseDto(0, "Could not recognize currency"));
    }
}
