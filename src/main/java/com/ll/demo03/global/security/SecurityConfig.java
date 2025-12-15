package com.ll.demo03.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/h2-console/**") // 이 부분은
                                .permitAll() // 허용
                                .requestMatchers("/actuator/**")
                                .permitAll()
                                .anyRequest() // 어떤 요청에도
                                .authenticated() // 인증되야한다

                ).headers( // h2 콘솔 들어가기위한 설정
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                ).csrf( // csrf옵션 끄기(restAPI 방식에서는 끄자, 타임리프,mpa 방식은 키자)
                        csrf ->
                                csrf.disable()
                ).formLogin(formLogin -> formLogin
                        .permitAll()
                );
        return http.build();
    }
}
