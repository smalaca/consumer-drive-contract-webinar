package com.smalaca.infrastructure.api.rest;

import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeRequestDto;
import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class CurrenciesServiceIntegrationTest {
    private static final String CURRENCIES_EXCHANGE = "http://localhost:8001/currency/";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void shouldExchangeEurToPln() {
        ExchangeRequestDto request = new ExchangeRequestDto("EUR", "PLN", 100, "2019-12-24");

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, request, ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(new ExchangeResponseDto(400, null));
    }

    @Test
    void shouldExchangePlnToUsd() {
        ExchangeRequestDto request = new ExchangeRequestDto("PLN", "USD", 900, "2020-01-28");

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, request, ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(new ExchangeResponseDto(1800, null));
    }

    @Test
    void shouldExchangeUnknown() {
        ExchangeRequestDto request = new ExchangeRequestDto("RUB", "USD", 900, "2020-01-28");

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, request, ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(new ExchangeResponseDto(0, "Could not recognize currency"));
    }
}