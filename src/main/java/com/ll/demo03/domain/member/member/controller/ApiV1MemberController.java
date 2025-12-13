package com.ll.demo03.domain.member.member.controller;

import com.ll.demo03.domain.member.member.dto.MemberDto;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import com.ll.demo03.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;

    // CRUD

    @AllArgsConstructor
    @Getter
    public static class MemberJoinRequestBody {
        @NotBlank(message = "아이디를 입력하여 주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력하여 주세요.")
        private String password;
        @NotBlank(message = "닉네임을 입력하여 주세요.")
        private String nickname;
    }

    @AllArgsConstructor
    @Getter
    public static class MemberJoinResponseBody {
        MemberDto item;
    }

    // Create
    // 위에 설정한 notblank 제약조건이 실행되게 할려면 받는 바디에 Valid 어노테이션 붙여야 함
    @PostMapping("")
    public RsData<MemberJoinResponseBody> join(
            @RequestBody @Valid MemberJoinRequestBody requestBody) {
        // 일부러 Exception 발생 시키기 위한 코드
//        int a = 40;
//        int b = 0;
//        int c = a / b;

        RsData<Member> joinRs = memberService.join(
                requestBody.username,
                requestBody.password,
                requestBody.nickname
        );

        Member member = joinRs.getData(); // 이해를 돋기 위한 코드 저런 타입이다.

        return joinRs.newDataOf(
                new MemberJoinResponseBody(
                        new MemberDto(member)
                )
        );
    }


}
