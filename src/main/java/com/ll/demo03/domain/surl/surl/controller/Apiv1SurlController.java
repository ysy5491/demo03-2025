package com.ll.demo03.domain.surl.surl.controller;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.dto.SurlDto;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.service.SurlService;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.global.rsData.RsData;
import com.ll.demo03.standard.dto.Empty;
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
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class Apiv1SurlController {
    private final SurlService surlService;
    private final Rq rq;

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
    public RsData<SurlGetRespBody> get(@PathVariable long id) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new); // :: 문법 공부하자 자바8에 도입됨 e404의 생성자를 참조하겠다는 뜻

        return RsData.of(
                new SurlGetRespBody(
                        new SurlDto(surl)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Empty> delete(@PathVariable long id) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        surlService.delete(surl);

        return RsData.OK;
    }

    // 다건 조회
    @AllArgsConstructor
    @Getter
    public static class SurlsGetRespBody {
        private List<SurlDto> items;
    }

    // api/v1/surls/{id}
    @GetMapping("")
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
    public RsData<SurlModifyRespBody> add(@PathVariable long id,
                                          @RequestBody @Valid SurlModifyReqBody reqBody) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        RsData<Surl> modifyRs = surlService.modify(surl, reqBody.body, reqBody.url);

        return modifyRs.newDataOf(
                new SurlModifyRespBody(
                        new SurlDto(modifyRs.getData())
                )
        );
    }
}
