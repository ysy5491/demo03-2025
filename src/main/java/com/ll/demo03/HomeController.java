package com.ll.demo03;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${custom.site.name}")
    private String siteName;

    @GetMapping("/")
    public String showMain() {
        return "Hello, World!, " + siteName;
    }
}

