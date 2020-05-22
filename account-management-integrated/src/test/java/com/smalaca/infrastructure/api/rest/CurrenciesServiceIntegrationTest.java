package com.smalaca.infrastructure.api.rest;

import com.smalaca.accountmanagement.infrastructure.api.rest.dto.ExchangeResponseDto;
import com.smalaca.infrastructure.api.rest.contract.ExchangeContract;
import com.smalaca.infrastructure.api.rest.contract.Scenario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class CurrenciesServiceIntegrationTest {
    private static final String CURRENCIES_EXCHANGE = "http://localhost:8001/currency/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ExchangeContract exchangeContract = new ExchangeContract();

    @Test
    void shouldExchangeEurToPln() {
        Scenario scenario = exchangeContract.eurToPln();

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, scenario.getRequest(), ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(scenario.getResponse());
    }

    @Test
    void shouldExchangePlnToUsd() {
        Scenario scenario = exchangeContract.plnToUsd();

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, scenario.getRequest(), ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(scenario.getResponse());
    }

    @Test
    void shouldExchangeUnknown() {
        Scenario scenario = exchangeContract.unknownCurrency();

        ExchangeResponseDto actual = restTemplate.postForEntity(CURRENCIES_EXCHANGE, scenario.getRequest(), ExchangeResponseDto.class).getBody();

        Assertions.assertThat(actual).isEqualTo(scenario.getResponse());
    }
}