package com.example.backend.service;

import com.example.backend.dto.CommentDto;
import com.example.backend.entity.Article;
import com.example.backend.entity.Comment;
import com.example.backend.entity.User;
import com.example.backend.repository.ArticleRepository;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 특정 게시글의 모든 댓글 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<CommentDto> getCommentsByArticle(Long articleId, Pageable pageable) {
        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId, pageable)
                .map(CommentDto::fromEntity);
    }
    
    // 특정 게시글의 모든 댓글 조회 (리스트)
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByArticleList(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 댓글 상세 조회
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return CommentDto.fromEntity(comment);
    }
    
    // 댓글 생성 (author 파라미터 제거)
    public CommentDto createComment(CommentDto commentDto, String username, Long articleId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        Comment comment = commentDto.toEntity(article, user);
        Comment savedComment = commentRepository.save(comment);
        
        return CommentDto.fromEntity(savedComment);
    }
    
    // 댓글 수정 (user ID로 권한 확인)
    public CommentDto updateComment(Long id, CommentDto commentDto, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 작성자 확인 (User ID로 비교)
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }
        
        comment.update(commentDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        
        return CommentDto.fromEntity(updatedComment);
    }
    
    // 댓글 삭제 (user ID로 권한 확인)
    public void deleteComment(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 작성자 확인 (User ID로 비교)
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }
        
        commentRepository.delete(comment);
    }
    
    // 특정 사용자의 댓글 조회
    @Transactional(readOnly = true)
    public Page<CommentDto> getCommentsByUser(Long userId, Pageable pageable) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(CommentDto::fromEntity);
    }
    
    // 댓글 내용으로 검색
    @Transactional(readOnly = true)
    public Page<CommentDto> searchByContent(String content, Pageable pageable) {
        return commentRepository.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(content, pageable)
                .map(CommentDto::fromEntity);
    }
    
    // 작성자 username으로 댓글 검색 (User 테이블 조인)
    @Transactional(readOnly = true)
    public Page<CommentDto> searchByUsername(String username, Pageable pageable) {
        return commentRepository.findByUserUsernameContainingIgnoreCaseOrderByCreatedAtDesc(username, pageable)
                .map(CommentDto::fromEntity);
    }
    
    // 특정 게시글의 댓글 수 조회
    @Transactional(readOnly = true)
    public Long getCommentCountByArticle(Long articleId) {
        return commentRepository.countByArticleId(articleId);
    }
    
    // 특정 사용자의 댓글 수 조회
    @Transactional(readOnly = true)
    public Long getCommentCountByUser(Long userId) {
        return commentRepository.countByUserId(userId);
    }
    
    // 최근 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentDto> getRecentComments(Pageable pageable) {
        return commentRepository.findRecentComments(pageable)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }
}