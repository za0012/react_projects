package com.example.backend.controller;

import com.example.backend.dto.CommentDto;
import com.example.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    // 특정 게시글의 댓글 조회 (페이징)
    @GetMapping("/article/{articleId}")
    public ResponseEntity<Page<CommentDto>> getCommentsByArticle(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<CommentDto> comments = commentService.getCommentsByArticle(articleId, pageable);
        
        return ResponseEntity.ok(comments);
    }
    
    // 특정 게시글의 댓글 조회 (리스트)
    @GetMapping("/article/{articleId}/list")
    public ResponseEntity<List<CommentDto>> getCommentsByArticleList(@PathVariable Long articleId) {
        List<CommentDto> comments = commentService.getCommentsByArticleList(articleId);
        return ResponseEntity.ok(comments);
    }
    
    // 댓글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        try {
            CommentDto comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 댓글 생성
    @PostMapping("/article/{articleId}")
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentDto commentDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            CommentDto createdComment = commentService.createComment(commentDto, username, articleId);
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 생성되었습니다.");
            response.put("data", createdComment);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDto commentDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            CommentDto updatedComment = commentService.updateComment(id, commentDto, username);
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 수정되었습니다.");
            response.put("data", updatedComment);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            commentService.deleteComment(id, username);
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 특정 사용자의 댓글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentDto>> getCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentDto> comments = commentService.getCommentsByUser(userId, pageable);
        
        return ResponseEntity.ok(comments);
    }
    
    // 댓글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<CommentDto>> searchComments(
            @RequestParam(required = false) String type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<CommentDto> comments;
        
        if (type == null || type.equals("content")) {
            comments = commentService.searchByContent(keyword, pageable);
        } else if (type.equals("author")) {
            comments = commentService.searchByUsername(keyword, pageable);
        } else {
            comments = commentService.searchByContent(keyword, pageable);
        }
        
        return ResponseEntity.ok(comments);
    }
    
    // 특정 게시글의 댓글 수 조회
    @GetMapping("/count/article/{articleId}")
    public ResponseEntity<Map<String, Object>> getCommentCountByArticle(@PathVariable Long articleId) {
        Map<String, Object> response = new HashMap<>();
        Long count = commentService.getCommentCountByArticle(articleId);
        
        response.put("articleId", articleId);
        response.put("commentCount", count);
        
        return ResponseEntity.ok(response);
    }
    
    // 최근 댓글 조회
    @GetMapping("/recent")
    public ResponseEntity<List<CommentDto>> getRecentComments(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<CommentDto> recentComments = commentService.getRecentComments(pageable);
        
        return ResponseEntity.ok(recentComments);
    }
    
    // 현재 사용자명 가져오기
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName();
        }
        return null;
    }
}