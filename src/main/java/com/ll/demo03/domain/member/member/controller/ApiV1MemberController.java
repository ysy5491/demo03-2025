package com.ll.demo03.domain.member.member.controller;

import com.ll.demo03.domain.auth.auth.service.AuthTokenService;
import com.ll.demo03.domain.member.member.dto.MemberDto;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.service.MemberService;
import com.ll.demo03.global.AppConfig;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.global.rsData.RsData;
import com.ll.demo03.standard.dto.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // swagger ui에서 해당 컨트롤러의 api를 사용할 때 인증이 필요하다는 것을 명시
@Transactional(readOnly = true)
@Tag(name = "ApiV1MemberController", description = "회원 CRUD 컨트롤러")
public class ApiV1MemberController {
    private final MemberService memberService;
    private final AuthTokenService authTokenService;
    private final Rq rq;
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
    @Transactional
    @Operation(summary = "회원가입")
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

    @AllArgsConstructor
    @Getter
    public static class MemberLoginRequestBody {
        @NotBlank(message = "아이디를 입력하여 주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력하여 주세요.")
        private String password;
    }

    @AllArgsConstructor
    @Getter
    public static class MemberLoginResponseBody {
        MemberDto item;
    }

    // Create
    // 위에 설정한 notblank 제약조건이 실행되게 할려면 받는 바디에 Valid 어노테이션 붙여야 함
    @PostMapping("/login")
    @Transactional
    @Operation(summary = "로그인", description = "로그인에 성공하면 쿠키에 accessToken, refreshToken이 발급됩니다.")
    public RsData<MemberLoginResponseBody> login(
            @RequestBody @Valid MemberLoginRequestBody requestBody
    ) {
        Member member = memberService.findByUsername(requestBody.username)
                .orElseThrow(() -> new GlobalException("401-1", "회원이 존재하지 않습니다."));

        if (!memberService.matchPassword(requestBody.password, member.getPassword()))
            throw new GlobalException("401-2", "비밀번호가 틀립니다.");

        // 쿠키 생성
        // 유저이름과 비번으로 구움
//        rq.setCookie("actorUserName", member.getUsername());
//        rq.setCookie("actorPassword", member.getPassword());
        // 쿠키에 apikey로 넣자!
        String accessToken = authTokenService.genToken(member, AppConfig.getAccessTokenExpirationSec()); // 1시간
        rq.setCookie("accessToken", accessToken);
        rq.setCookie("refreshToken", member.getRefreshToken());

        return RsData.of(
                "200-1",
                "로그인 되었습니다.",
                new MemberLoginResponseBody(
                        new MemberDto(member)
                )
        );
    }

    @DeleteMapping("/logout")
    @Transactional
    @Operation(summary = "로그아웃")
    public RsData<Empty> logout() {
        // 쿠키 삭제
//        rq.removeCookie("actorUserName");
//        rq.removeCookie("actorPassword");
//        rq.removeCookie("apiKey");
        rq.removeCookie("refreshToken");
        rq.removeCookie("accessToken");
        return RsData.OK;
    }
    @AllArgsConstructor
    @Getter
    private static class MemberMeResponseBody {
        MemberDto item;
    }

    @GetMapping("/me")
    @Transactional
    @Operation(summary = "내 정보", description = "로그인한 회원의 정보를 반환합니다.")
    public RsData<MemberMeResponseBody> me() {
        Member member = rq.getMember();

        return RsData.of(
                new MemberMeResponseBody(
                        new MemberDto(member)
                )
        );
    }

}
