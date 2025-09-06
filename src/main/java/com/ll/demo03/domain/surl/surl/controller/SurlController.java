package com.ll.demo03.domain.surl.surl.controller;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.service.SurlService;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.global.rsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SurlController {
    private final Rq rq;
    private final SurlService surlService;

    @GetMapping("/all")
    @ResponseBody
    public List<Surl> getAll(String body, String url) {
        return surlService.findAll();
    }

    @GetMapping("/add")
    @ResponseBody
    public RsData<Surl> add(String body, String url){
        Member member = rq.getMember(); // 현재 브라우저로 로그인한 회원

        log.debug("log test"); // application.yml에 로깅레벨을 info로 올렸기 때문에 운영모드에서는 debug레벨은 보여지지 않는다

        return surlService.add(member, body, url);
    }

    @GetMapping("/s/{body}/**")
    @ResponseBody
    public RsData<Surl> add(@PathVariable String body, HttpServletRequest req){
        String url = req.getRequestURI();

        if (req.getQueryString() != null) {
            url += "?" + req.getQueryString();
        }
        Member member = rq.getMember();

        String[] urlBits = url.split("/", 4);

        url = urlBits[3];

        return surlService.add(member, body, url);
    }

    @GetMapping("/g/{id}")
    public String go(@PathVariable long id){
        Surl surl = surlService.findyById(id).orElseThrow(GlobalException.E404::new);

        surlService.increaseCount(surl);

        return "redirect:" + surl.getUrl();
    }
}
