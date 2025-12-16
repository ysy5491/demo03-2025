package com.ll.demo03.global.initData;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

// !Prod == dev or test
@Configuration
@RequiredArgsConstructor
@Slf4j
public class All {

    @Lazy
    @Autowired
    private All self;
    private final MemberService memberService;

    @Bean
    @Order(3)
    public ApplicationRunner iniAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        log.debug("initAll");

        if (memberService.count() > 0) return;

        Member memberSystem = memberService.join("system", "5491", "시스템").getData();
        Member memberAdmin = memberService.join("admin", "5491", "어드민").getData();

        Member memberUser1 = memberService.join("user1", "5491", "유저1").getData();
        Member memberUser2 = memberService.join("user2", "5491", "유저2").getData();
        Member memberUser3 = memberService.join("ysy5491", "5491", "윤지환").getData();
//        Article article1 = articleService.write(memberSystem, "title1", "body1").getData();
//        Article article2 = articleService.write(memberAdmin, "title2", "body2").getData();

    }
}
