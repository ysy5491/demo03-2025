package com.ll.demo03.global.rq;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.standard.dto.utill.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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


        // body로 파싱
        String actorUserName = req.getParameter("actorUserName"); // 뭔지 찾아보자
        String actorPassword = req.getParameter("actorPassword");

        // header로 파싱
        if (actorUserName == null) actorUserName = req.getHeader("actorUserName");
        if (actorPassword == null) actorPassword = req.getHeader("actorPassword");

        if(Ut.str.isBlank(actorUserName)) throw new GlobalException("401-1", "인증정보(아이디)를 입력해주세요.");
        if(Ut.str.isBlank(actorPassword)) throw new GlobalException("401-2", "인증정보(password)를 입력해주세요.");

        Member loginedMember = memberService.findByUsername(actorUserName).orElseThrow(() -> new GlobalException("403-3", "해당 회원이 존재하지 않습니다."));
        if (!loginedMember.getPassword().equals(actorPassword)) throw new GlobalException("403-4", "비밀번호가 틀립니다.");

        member = loginedMember;

        return loginedMember;
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }
}
