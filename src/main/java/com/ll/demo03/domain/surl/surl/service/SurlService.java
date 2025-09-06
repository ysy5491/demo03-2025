package com.ll.demo03.domain.surl.surl.service;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.repository.SurlRepository;
import com.ll.demo03.global.rsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SurlService {
    private final SurlRepository surlRepository;
    public List<Surl> findAll() {
        return surlRepository.findAll();
    }

    @Transactional
    public RsData<Surl> add(Member author, String body, String url) {
        Surl surl = Surl.builder()
                .member(author)
                .body(body)
                .url(url)
                .build();
        surlRepository.save(surl);
        return RsData.of("%d번 url이 생성되었습니다.".formatted(surl.getId()), surl);
    }

    public Optional<Surl> findyById(Long id) {
        return surlRepository.findById(id);
    }

    @Transactional
    public void increaseCount(Surl surl) {
        surl.increaseCount();
    }
}
