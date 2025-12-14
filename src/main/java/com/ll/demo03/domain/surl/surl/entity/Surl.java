package com.ll.demo03.domain.surl.surl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Surl extends BaseTime {
    @ManyToOne
    @JsonIgnore
    private Member author;
    private String body;
    private String url;
    @Setter(AccessLevel.NONE)
    private long count;

    public void increaseCount() {
        count++;
    }
}
