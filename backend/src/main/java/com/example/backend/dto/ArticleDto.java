package com.example.backend.dto;

import java.time.LocalDateTime;

import com.example.backend.entity.Article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class ArticleDto {
    private Long id;
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력하세요.")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    
    private String author;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer commentCount;
    
    // // 기본 생성자
    // public ArticleDto() {}
    
    // 생성자
    public ArticleDto(Long id, String title, String content, String author, 
                     Integer viewCount, LocalDateTime createdAt, LocalDateTime updatedAt, 
                     Integer commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
    }
    
    // Entity -> DTO 변환
    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
            article.getId(),
            article.getTitle(),
            article.getContent(),
            article.getUser().getUsername(),  // User 엔티티에서 username 가져오기
            article.getViewCount(),
            article.getCreatedAt(),
            article.getUpdatedAt(),
            article.getComments() != null ? article.getComments().size() : 0
        );
    }
    
    // DTO -> Entity 변환 (새 게시글 생성용)
    public Article toEntity(String author, com.example.backend.entity.User user) {
        return new Article(this.title, this.content, author, user);
    }

}