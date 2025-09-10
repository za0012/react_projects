package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder 전용으로만 사용
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    @Column(nullable = false)
    private String content;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // 생성자 (author 파라미터 제거)
    public Comment(String content, Article article, User user) {
        this.content = content;
        this.article = article;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 업데이트 메서드
    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    // 작성자 username을 가져오는 편의 메서드
    public String getAuthorName() {
        return user != null ? user.getUsername() : null;
    }
}