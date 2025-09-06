package com.ll.demo03.domain.article.article.entity;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Article extends BaseTime {
    private String title;
    @Column(columnDefinition = "Text")
    private String body;
    @ManyToOne // To를 기준으로 왼쪽이 클래스 오른쪽이 멤버로 생각하면 됨 ex) member가 여러개의 article 작성 가능
    private Member author;
}
