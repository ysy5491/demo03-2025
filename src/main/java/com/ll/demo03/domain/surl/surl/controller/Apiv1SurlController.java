package com.ll.demo03.domain.surl.surl.controller;

import com.ll.demo03.domain.auth.auth.service.AuthService;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.dto.SurlDto;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.service.SurlService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/surls")
@RequiredArgsConstructor // lombok 어노테이션으로 생성자 자동 생성(어떤 생성자냐면 final이 붙은 필드들을 매개변수로 받는 생성자)
@Slf4j
@SecurityRequirement(name = "bearerAuth") // swagger ui에서 해당 컨트롤러의 api를 사용할 때 인증이 필요하다는 것을 명시
@Transactional(readOnly = true)
@Tag(name = "Apiv1SurlController", description = "surl CRUD 컨트롤러")
public class Apiv1SurlController {
    private final SurlService surlService;
    private final Rq rq;
    private final AuthService authService;

    @AllArgsConstructor
    @Getter
    public static class SurlAddReqBody {
        @NotBlank
        private String body;
        @NotBlank
        private String url;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlAddRespBody {
        private SurlDto item;
    }

    @PostMapping("")
    @Transactional
    @Operation(summary = "surl 추가")
    public RsData<SurlAddRespBody> add(@RequestBody @Valid SurlAddReqBody reqBody) {
        Member member = rq.getMember();

        RsData<Surl> addRs = surlService.add(member, reqBody.body, reqBody.url);
        return addRs.newDataOf(
                new SurlAddRespBody(
                        new SurlDto(addRs.getData())
                )
        );
    }

    @AllArgsConstructor
    @Getter
    public static class SurlGetRespBody {
        private SurlDto item;
    }

    // api/v1/surls/{id}
    @GetMapping("/{id}")
    @Operation(summary = "surl 단건 조회")
    public RsData<SurlGetRespBody> get(
            @PathVariable long id
    ) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new); // :: 문법 공부하자 자바8에 도입됨 e404의 생성자를 참조하겠다는 뜻

        Member member = rq.getMember();

        authService.checkCanGetSurl(member, surl);

        return RsData.of(
                new SurlGetRespBody(
                        new SurlDto(surl)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "surl 삭제")
    public RsData<Empty> delete(@PathVariable long id) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        Member member = rq.getMember();

        authService.checkCanDeleteSurl(member, surl);

        surlService.delete(surl);

        return RsData.OK;
    }

    // 다건 조회
    @AllArgsConstructor
    @Getter
    public static class SurlsGetRespBody {
        private List<SurlDto> items;
    }


    @GetMapping("")
    @Operation(summary = "surl 목록 조회")
    public RsData<SurlsGetRespBody> getItems() {

        Member member = rq.getMember();
        // page 기능 제공
        // QueryDSL

        List<Surl> surls = surlService.findByAuthorOrderByIdDesc(member);

        return RsData.of(
                new SurlsGetRespBody(
                        surls.stream()
                                .map(SurlDto::new)
                                .toList() // 이 부분 추가 공부하자!
                        )
        );
    }

    @AllArgsConstructor
    @Getter
    public static class SurlModifyReqBody {
        @NotBlank
        private String body;
        @NotBlank
        private String url;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlModifyRespBody {
        private SurlDto item;
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "surl 수정")
    public RsData<SurlModifyRespBody> add(@PathVariable long id,
                                          @RequestBody @Valid SurlModifyReqBody reqBody) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);
        Member member = rq.getMember();

        authService.checkCanModifySurl(member, surl);

        RsData<Surl> modifyRs = surlService.modify(surl, reqBody.body, reqBody.url);

        return modifyRs.newDataOf(
                new SurlModifyRespBody(
                        new SurlDto(modifyRs.getData())
                )
        );
    }
}
