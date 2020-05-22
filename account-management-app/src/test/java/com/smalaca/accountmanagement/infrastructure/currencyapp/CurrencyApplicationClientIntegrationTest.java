package com.smalaca.accountmanagement.infrastructure.currencyapp;

import com.smalaca.cdc.contract.ContractReader;
import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CurrencyApplicationClientIntegrationTest {
    private static final int EUR_TO_PLN = 0;
    private static final int PLN_TO_USD = 1;
    private static final int UNKNOWN_CURRENCY = 2;

    @Autowired
    private CurrencyApplicationClient currencyApplicationClient;
    private final ContractReader contractReader = new ContractReader();

    @Test
    void shouldExchangeCurrencies() {
        Map<Input, Output> scenarios = contractReader.readAll();

        assertThat(scenarios.size()).isEqualTo(3);
        scenarios.forEach((key, value) -> assertThat(currencyApplicationClient.exchange(key)).isEqualTo(value));
    }

    @Test
    void shouldExchangeFromEuroToPln() {
        Output actual = currencyApplicationClient.exchange(contractReader.inputForScenario(EUR_TO_PLN));

        assertThat(actual).isEqualTo(contractReader.outputForScenario(EUR_TO_PLN));
    }

    @Test
    void shouldExchangeFromPlnToUsd() {
        Output actual = currencyApplicationClient.exchange(contractReader.inputForScenario(PLN_TO_USD));

        assertThat(actual).isEqualTo(contractReader.outputForScenario(PLN_TO_USD));
    }

    @Test
    void shouldHandleUnknownCurrency() {
        Output actual = currencyApplicationClient.exchange(contractReader.inputForScenario(UNKNOWN_CURRENCY));

        assertThat(actual).isEqualTo(contractReader.outputForScenario(UNKNOWN_CURRENCY));
    }
}