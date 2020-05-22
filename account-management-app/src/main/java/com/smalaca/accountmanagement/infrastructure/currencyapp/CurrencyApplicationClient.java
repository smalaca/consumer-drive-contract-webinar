package com.smalaca.accountmanagement.infrastructure.currencyapp;

import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyApplicationClient {
    private static final String CURRENCIES_EXCHANGE = "http://localhost:8001/currency/";

    private final RestTemplate restTemplate = new RestTemplate();

    public Output exchange(Input input) {
        return restTemplate.postForEntity(CURRENCIES_EXCHANGE, input, Output.class).getBody();
    }
}
