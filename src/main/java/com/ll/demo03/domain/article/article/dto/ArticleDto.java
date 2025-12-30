package com.ll.demo03.domain.article.article.dto;

import com.ll.demo03.domain.article.article.entity.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private String title;
    @NonNull
    private String body;
    @NonNull
    private long authorId;
    @NonNull
    private String authorName;
    public ArticleDto(Article article) {
        this.id = article.getId();
        this.createDate = article.getCreateDate();
        this.modifyDate = article.getModifyDate();
        this.title = article.getTitle();
        this.body = article.getBody();
        this.authorId = article.getAuthor().getId();
        this.authorName = article.getAuthor().getName();
    }
}
