package com.ll.demo03.domain.surl.surl.controller;

import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.service.SurlService;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Tag(name = "SurlController", description = "surl 컨트롤러")
public class SurlController {
    private final Rq rq;
    private final SurlService surlService;

//    @GetMapping("/all")
//    @ResponseBody
//    public List<Surl> getAll(String body, String url) {
//        return surlService.findAll();
//    }

//    @GetMapping("/add")
//    @ResponseBody
//    @Transactional
//    public RsData<Surl> add(String body, String url){
//        Member member = rq.getMember(); // 현재 브라우저로 로그인한 회원
//
//        log.debug("log test"); // application.yml에 로깅레벨을 info로 올렸기 때문에 운영모드에서는 debug레벨은 보여지지 않는다
//
//        return surlService.add(member, body, url);
//    }

//    @GetMapping("/s/{body}/**")
//    @ResponseBody
//    @Transactional
//    public RsData<Surl> add(@PathVariable String body, HttpServletRequest req){
//        String url = req.getRequestURI();
//
//        if (req.getQueryString() != null) {
//            url += "?" + req.getQueryString();
//        }
//        Member member = rq.getMember();
//
//        String[] urlBits = url.split("/", 4);
//
//        url = urlBits[3];
//
//        return surlService.add(member, body, url);
//    }

    @GetMapping("/g/{id}")
    @Transactional
    @Operation(summary = "원본 url로 리다이렉트")
    public String go(@PathVariable long id){
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        surlService.increaseCount(surl);

        return "redirect:" + surl.getUrl();
    }
}
