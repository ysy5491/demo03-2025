package com.ll.demo03.global.jpa.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 애노테이션이 붙은 엔티티 클래스를 상속받아야 jpa가 컬럼 인식을 함(없으면 코드 상에서는 상속 받지만 실제 db 컬렁에는 반영 X)
@Getter
@Setter(AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 두 애노테이션이 작동되게 하기 위해 작성
public abstract class BaseTime extends BaseEntity {
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}