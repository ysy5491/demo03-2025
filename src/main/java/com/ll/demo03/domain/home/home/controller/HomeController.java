package com.ll.demo03.domain.home.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(readOnly = true)
@Tag(name = "HomeController", description = "백엔드 홈 컨트롤러, 특별한 기능은 없음")
public class HomeController {

    @Value("${custom.site.name}")
    private String siteName;

    @Operation(summary = "메인화면을 보여주는 역할, 특별한, 특별한 기능은 없음")
    @GetMapping("/")
    public String showMain() {
        return "지환이의 사이트!!!!!, " + siteName;
    }

//    @Value("${custom.secret.key}")
//    private String secretKey;
//
//    @GetMapping("/secretKey")
//    public String showSecretKey() {
//        return "SecretKey: " + secretKey;
//    }
}

