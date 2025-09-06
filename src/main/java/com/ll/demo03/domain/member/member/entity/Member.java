package com.ll.demo03.domain.member.member.entity;

import com.ll.demo03.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member extends BaseTime {
    private String username;
    private String password;
    private String nickname;
}
