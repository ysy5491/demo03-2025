package com.ll.demo03.global.rq;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req; // 이거 알고 넘어가자!
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private Member member;

    public Member getMember() {
        if (member != null) return member; // 캐싱 방식(한번의 요청에 getmember가 여러번 쓰일수도 있기 때문에 안전장치)

        // ==================현재 이 인증 로직은 CustomAuthentication에 있음=================

        // 쿠키는 클라이언트와 서버가 공유하는 변수로 생각하자
//        String actorUserName = getCookieValue("actorUserName", null);
//        String actorPassword = getCookieValue("actorPassword", null);
        // 쿠키 인증방식 종료

        // body로 파싱
//        String actorUserName = req.getParameter("actorUserName"); // 뭔지 찾아보자
//        String actorPassword = req.getParameter("actorPassword");

        // header로 파싱
//        if (actorUserName == null) actorUserName = req.getHeader("actorUserName");
//        if (actorPassword == null) actorPassword = req.getHeader("actorPassword");


        // Bearer 토큰 사용 (토큰에 이름 비번 삽입)
//        if (actorUserName == null || actorPassword == null) {
//            String authorization = req.getHeader("Authorization");
//            if (authorization != null) {
//                authorization = authorization.substring("Bearer ".length()).trim();
//                String[] authorizationBits = authorization.split(" ", 2);
//                actorUserName = authorizationBits[0];
//                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null; // 삼항연산자 조건 ? 참일때 값 : 거짓일때 값
//            }
//        }

//        if(Ut.str.isBlank(actorUserName)) throw new GlobalException("401-1", "인증정보(아이디)를 입력해주세요.");
//        if(Ut.str.isBlank(actorPassword)) throw new GlobalException("401-2", "인증정보(password)를 입력해주세요.");
//
//        Member loginedMember = memberService.findByUsername(actorUserName).orElseThrow(() -> new GlobalException("403-3", "해당 회원이 존재하지 않습니다."));
//        if (!memberService.matchPassword(actorPassword, loginedMember.getPassword())) throw new GlobalException("403-4", "비밀번호가 틀립니다.");
// ======================================================================================
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberService.findByUsername(name).get();

        return member;
    }


    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

    // 쿠키 관련 시작
    public String getCookieValue(String cookieName, String defaultValue) {
//        if (req.getCookies() != null) {
//            for (Cookie cookie : req.getCookies()) {
//                if (cookie.getName().equals(cookieName)) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return defaultValue;
        // 스트림 방식 (강사피셜: 이게 보기 더 깔끔하다고 함)
        if (req.getCookies() == null) return defaultValue;

        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(defaultValue);
    }

    public void removeCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/"); // 모든 페이지(루)
        resp.addCookie(cookie);
    }

    public void setCookie(String cookieName, String name) {
        Cookie cookie = new Cookie(cookieName, name);
        // 1년
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
    // 쿠키 관련 끝
}
