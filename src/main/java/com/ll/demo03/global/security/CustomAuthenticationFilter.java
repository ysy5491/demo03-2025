package com.ll.demo03.global.security;

import com.ll.demo03.domain.auth.auth.service.AuthTokenService;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import com.ll.demo03.global.AppConfig;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.standard.utill.utill.Ut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;
    private final AuthTokenService authTokenService;

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

        String accessToken = rq.getCookieValue("accessToken", null);
        String refreshToken = rq.getCookieValue("refreshToken", null);

        // 특정 경로는 필터링하지 않음(swagger)
        String path = request.getRequestURI();
        if (
                path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/swagger-ui.html")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken == null || refreshToken == null) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                // Bearer 토큰 사용 (토큰에 이름 비번 삽입)
                String[] authorizationBits = authorization.substring("Bearer ".length()).trim().split(" ", 2);
                // 토큰이 2개여야 함
                if (authorizationBits.length == 2) {
                    accessToken = authorizationBits[0];
                    refreshToken = authorizationBits[1];
                }
            }
        }

        if (Ut.str.isBlank(accessToken) || Ut.str.isBlank(refreshToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 검증
        if (!authTokenService.validateToken(accessToken)) {
            // 액세스 토큰이 유효하지 않은 경우
            Member member = memberService.findByRefreshToken(refreshToken).orElse(null);
            // 리프레시 토큰도 유효하지 않으면 인증 실패
            if (member == null) {
                filterChain.doFilter(request, response);
                return;
            }
            // 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급
            String newAccessToken = authTokenService.genToken(member, AppConfig.getAccessTokenExpirationSec()); // 1시간
            rq.setCookie("accessToken", newAccessToken); // 새로운 액세스 토큰으로 쿠키 갱신
            log.debug("New Access Token renewd: " + newAccessToken);

            accessToken = newAccessToken; // 이후 인증 처리를 위해 액세스 토큰을 새로 발급한 토큰으로 교체
        }

        // 토큰에서 데이터 가져오기
        Map<String, Object> accessTokenData = authTokenService.getDataFrom(accessToken);

        long id = (int) accessTokenData.get("id");
        // 인증 완료 처리
        User user = new User(id + "", "", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response); // 필터를 종료하고 다음 턴으로 넘긴다.
    }
}