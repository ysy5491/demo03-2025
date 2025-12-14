package com.ll.demo03.domain.surl.surl.service;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.surl.surl.entity.Surl;
import com.ll.demo03.domain.surl.surl.repository.SurlRepository;
import com.ll.demo03.global.rsData.RsData;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
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
                .author(author)
                .body(body)
                .url(url)
                .build();
        surlRepository.save(surl);
        return RsData.of("%d번 surl이 생성되었습니다.".formatted(surl.getId()), surl);
    }

    public Optional<Surl> findById(Long id) {
        return surlRepository.findById(id);
    }

    @Transactional
    public void increaseCount(Surl surl) {
        surl.increaseCount();
    }

    public void delete(Surl surl) {
        surlRepository.deleteById(surl.getId());
    }

    public List<Surl> findByAuthorOrderByIdDesc(Member author) {
        return surlRepository.findByAuthorOrderByIdDesc(author);
    }

    @Transactional
    public RsData<Surl> modify(Surl surl, @NotBlank String body, @NotBlank String url) {
        surl.setBody(body);
        surl.setUrl(url);

        return RsData.of("%d번 SURL이 수정되었습니다.".formatted(surl.getId()), surl);
    }
}
