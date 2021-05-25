package ru.kuznetsov.loyaltymanagement.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class TestService {

    @GetMapping()
    public String test() {
        return "hello123";
    }
}
