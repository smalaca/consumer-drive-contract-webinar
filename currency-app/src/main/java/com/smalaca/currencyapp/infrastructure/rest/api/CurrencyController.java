package com.smalaca.currencyapp.infrastructure.rest.api;

import com.smalaca.cdc.contract.ContractReader;
import com.smalaca.cdc.contract.Input;
import com.smalaca.cdc.contract.Output;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/currency")
class CurrencyController {
    private final Map<Input, Output> scenarios = new ContractReader().readAll();

    @PostMapping
    public Output convert(@RequestBody Input input) {
        return scenarios.get(input);
    }
}
