package com.example.backend.dto;

import com.example.backend.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class CommentDto {
    private Long id;
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    
    private String authorName; // user.name에서 가져온 작성자 이름
    private Long userId;       // 작성자 ID 추가
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 생성자 (author -> authorName, userId 추가)
    public CommentDto(Long id, String content, String authorName, Long userId, Long articleId, 
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.userId = userId;
        this.articleId = articleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Entity -> DTO 변환
    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getContent(),
            comment.getAuthorName(), // user.getUsername()으로 가져오기
            comment.getUser() != null ? comment.getUser().getId() : null,
            comment.getArticle() != null ? comment.getArticle().getId() : null,
            comment.getCreatedAt(),
            comment.getUpdatedAt()
        );
    }
    
    // DTO -> Entity 변환 (새 댓글 생성용) - author 파라미터 제거
    public Comment toEntity(com.example.backend.entity.Article article, 
                           com.example.backend.entity.User user) {
        return new Comment(this.content, article, user);
    }
}