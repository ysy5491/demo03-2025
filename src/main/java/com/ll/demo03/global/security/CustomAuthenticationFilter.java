package com.ll.demo03.global.security;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.standard.utill.utill.Ut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        System.out.println("CustomAuthenticationFilter.doFilterInternal");

//        String actorUserName = rq.getCookieValue("actorUserName", null);
//        String actorPassword = rq.getCookieValue("actorPassword", null);

//        if (actorUserName == null || actorPassword == null) {
//            String authorization = request.getHeader("Authorization");
//            if (authorization != null) {
//                authorization = authorization.substring("Bearer ".length()).trim();
//                String[] authorizationBits = authorization.split(" ", 2);
//                actorUserName = authorizationBits[0];
//                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null; // 삼항연산자 조건 ? 참일때 값 : 거짓일때 값
//            }
//        }

//        if (!memberService.matchPassword(actorPassword, loginedMember.getPassword())) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String apiKey = rq.getCookieValue("apiKey", null);

        if (apiKey == null) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                apiKey = authorization.substring("Bearer ".length()).trim();
            }
        }

        if (Ut.str.isBlank(apiKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        Member loginedMember = memberService.findByApiKey(apiKey).orElse(null);

        if (loginedMember == null) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = new User(loginedMember.getId() + "", "", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response); // 필터를 종료하고 다음 턴으로 넘긴다.
        return;
    }
}