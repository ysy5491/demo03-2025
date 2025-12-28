package com.ll.demo03.global.security;

import com.ll.demo03.global.rsData.RsData;
import com.ll.demo03.standard.utill.utill.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/members", "/api/*/members/login", "/api/*/members/logout") // post요청으로오는 저 url
                                .permitAll()// 허용
                                .requestMatchers("/h2-console/**") // 이 부분은
                                .permitAll() // 모든사람이 허용
                                .requestMatchers("/actuator/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/g/**")
                                .permitAll()
                                .anyRequest() // 어떤 요청에도
                                .authenticated() // 인증되야한다

                )
                .headers( // h2 콘솔 들어가기위한 설정
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf( // csrf옵션 끄기(restAPI 방식에서는 끄자, 타임리프,mpa 방식은 키)
                        csrf ->
                                csrf.disable()
                )
                .formLogin(formLogin -> formLogin
                        .permitAll()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");
                                            response.setStatus(403);
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            RsData.of("403-1", request.getRequestURI() + ", " + authException.getLocalizedMessage())
                                                    )
                                            );
                                        }
                                )
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
