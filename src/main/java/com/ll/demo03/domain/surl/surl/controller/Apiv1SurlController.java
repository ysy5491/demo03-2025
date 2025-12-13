package com.ll.demo03.domain.surl.surl.controller;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.dto.SurlDto;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.service.SurlService;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surls")
@RequiredArgsConstructor
@Slf4j
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
        Surl surl = surlService.findyById(id).orElseThrow(GlobalException.E404::new); // :: 문법 공부하자 자바8에 도입됨 e404의 생성자를 참조하겠다는 뜻

        return RsData.of(
                new SurlGetRespBody(
                        new SurlDto(surl)
                )
        );
    }
}
