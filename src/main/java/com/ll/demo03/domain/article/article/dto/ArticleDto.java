package com.ll.demo03.domain.article.article.dto;

import com.ll.demo03.domain.article.article.entity.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String title;
    private String body;
    private long authorId;
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
