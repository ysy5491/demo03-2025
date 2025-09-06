package com.ll.demo03.domain.home.home.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${custom.site.name}")
    private String siteName;

    @Value("${custom.secret.key}")
    private String secretKey;

    @GetMapping("/")
    public String showMain() {
        return "Hello, World!, " + siteName;
    }

    @GetMapping("/secretKey")
    public String showSecretKey() {
        return "SecretKey: " + secretKey;
    }
}

