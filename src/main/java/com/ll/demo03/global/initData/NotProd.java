package com.ll.demo03.global.initData;

import com.ll.demo03.domain.article.article.entity.Article;
import com.ll.demo03.domain.article.article.service.ArticleService;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

// !Prod == dev or test
@Profile("!Prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {

    @Lazy
    @Autowired
    private NotProd self;
    private final ArticleService articleService;
    private final MemberService memberService;

    @Bean
    @Order(4)
    public ApplicationRunner initNotProd() {
        return args -> {
            System.out.println("NotProd.initNotProd");
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (articleService.count() > 0) return;

        Member memberUser1 = memberService.findByUsername("user1").get();
        Member memberUser2 = memberService.findByUsername("user2").get();

        Article article1 = articleService.write(memberUser1, "title1", "body1").getData();
        Article article2 = articleService.write(memberUser1, "title2", "body2").getData();

        Article article3 = articleService.write(memberUser2, "title3", "body1").getData();
        Article article4 = articleService.write(memberUser2, "title4", "body2").getData();

    }
}
