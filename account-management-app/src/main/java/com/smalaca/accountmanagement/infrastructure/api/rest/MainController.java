package com.smalaca.accountmanagement.infrastructure.api.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String hello() {
        return "Account Management Application Welcome!";
    }
}
