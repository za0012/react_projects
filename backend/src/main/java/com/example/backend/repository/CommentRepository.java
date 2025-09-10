package com.example.backend.repository;

import com.example.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글의 댓글 조회 (페이징)
    Page<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId, Pageable pageable);
    
    // 특정 게시글의 댓글 조회 (리스트)
    List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);
    
    // 특정 사용자의 댓글 조회 (페이징)
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 새로 추가할 메서드 (User 테이블과 조인해서 username으로 검색):
    @Query("SELECT c FROM Comment c JOIN c.user u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY c.createdAt DESC")
    Page<Comment> findByUserUsernameContainingIgnoreCaseOrderByCreatedAtDesc(@Param("username") String username, Pageable pageable);
    
    // 특정 게시글의 댓글 수 조회
    Long countByArticleId(Long articleId);
    
    // 특정 사용자의 댓글 수 조회
    Long countByUserId(Long userId);
    
    // 최근 댓글 조회
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findRecentComments(Pageable pageable);
    
    // 특정 기간의 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.createdAt >= :startDate AND c.createdAt <= :endDate ORDER BY c.createdAt DESC")
    List<Comment> findCommentsByDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                                        @Param("endDate") java.time.LocalDateTime endDate);
    
    // 댓글 내용으로 검색
    Page<Comment> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String content, Pageable pageable);
}