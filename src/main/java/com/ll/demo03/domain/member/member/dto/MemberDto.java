package com.ll.demo03.domain.member.member.dto;

import com.ll.demo03.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberDto {
    @NonNull
    private Long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private String nickname;
    @NonNull
    private String username;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
    }
}
