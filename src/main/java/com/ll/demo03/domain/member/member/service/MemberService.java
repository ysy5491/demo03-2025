package com.ll.demo03.domain.member.member.service;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.domain.member.member.repository.MemberRepository;
import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    @Transactional
    public RsData<Member> join(String username, String password, String nickname) {
        findByUsername(username).ifPresent(member -> {
            throw new GlobalException("400-1", "%s은(는) 이미 존재하는 닉네임".formatted(member.getUsername()));
        });

        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
        memberRepository.save(member);
        return RsData.of("회원가입 완료", member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // getReferenceById는 프록시 객체를 반환 pk만 알고 있고 실제 그 외에 실제 조회가 필요할때 select 실행 (성능 최적화)
    public Member getById(long id) {
        return memberRepository.getReferenceById(1L);
    }

    public long count() {
        return memberRepository.count();
    }
}
