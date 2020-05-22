package com.smalaca.infrastructure.api.rest.contract;

import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeRequestDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;

public class Scenario {
    private final ExchangeRequestDto request;
    private final ExchangeResponseDto response;

    public Scenario(ExchangeRequestDto request, ExchangeResponseDto response) {
        this.request = request;
        this.response = response;
    }

    public ExchangeRequestDto getRequest() {
        return request;
    }

    public ExchangeResponseDto getResponse() {
        return response;
    }
}
