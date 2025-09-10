package com.example.backend.repository;

import com.example.backend.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    // 제목으로 검색 (페이징)
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // 내용으로 검색 (페이징)
    Page<Article> findByContentContainingIgnoreCase(String content, Pageable pageable);
    
    // 작성자로 검색 (페이징)
    Page<Article> findByUser_UsernameContainingIgnoreCase(String username, Pageable pageable);
    
    // 제목 또는 내용으로 검색 (페이징)
    Page<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
        String title, String content, Pageable pageable);
    
    // 특정 사용자의 게시글 조회
    Page<Article> findByUserId(Long userId, Pageable pageable);
    
    // 조회수 순으로 인기 게시글 조회
    @Query("SELECT a FROM Article a ORDER BY a.viewCount DESC")
    List<Article> findTopByViewCount(Pageable pageable);
    
    // 최신 게시글 조회
    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    List<Article> findRecentArticles(Pageable pageable);
    
    // 댓글 수가 많은 게시글 조회
    @Query("SELECT a FROM Article a LEFT JOIN a.comments c GROUP BY a ORDER BY COUNT(c) DESC")
    List<Article> findTopByCommentCount(Pageable pageable);
    
    // 특정 기간의 게시글 수 조회
    @Query("SELECT COUNT(a) FROM Article a WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate")
    Long countArticlesByDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                                 @Param("endDate") java.time.LocalDateTime endDate);
}