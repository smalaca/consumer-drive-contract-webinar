package com.smalaca.currencyapp.infrastructure.rest.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String hello() {
        return "Currency Application Welcome!";
    }
}
