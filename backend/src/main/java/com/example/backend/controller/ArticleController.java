package com.example.backend.controller;

import com.example.backend.dto.ArticleDto;
import com.example.backend.service.ArticleService;
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
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    // 모든 게시글 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ArticleDto> articles = articleService.getAllArticles(pageable);
        
        return ResponseEntity.ok(articles);
    }
    
    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        try {
            ArticleDto article = articleService.getArticleById(id);
            return ResponseEntity.ok(article);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 게시글 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createArticle(@Valid @RequestBody ArticleDto articleDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            ArticleDto createdArticle = articleService.createArticle(articleDto, username);
            response.put("success", true);
            response.put("message", "게시글이 성공적으로 생성되었습니다.");
            response.put("data", createdArticle);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable Long id, 
            @Valid @RequestBody ArticleDto articleDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            ArticleDto updatedArticle = articleService.updateArticle(id, articleDto, username);
            response.put("success", true);
            response.put("message", "게시글이 성공적으로 수정되었습니다.");
            response.put("data", updatedArticle);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteArticle(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            articleService.deleteArticle(id, username);
            response.put("success", true);
            response.put("message", "게시글이 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<ArticleDto>> searchArticles(
            @RequestParam(required = false) String type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, 
                           Sort.by("createdAt").descending());
        
        Page<ArticleDto> articles;
        
        if (type == null || type.equals("all")) {
            articles = articleService.searchByTitleOrContent(keyword, pageable);
        } else if (type.equals("title")) {
            articles = articleService.searchByTitle(keyword, pageable);
        } else if (type.equals("content")) {
            articles = articleService.searchByContent(keyword, pageable);
        } else if (type.equals("author")) {
            articles = articleService.searchByUsername(keyword, pageable);
        } else {
            articles = articleService.searchByTitleOrContent(keyword, pageable);
        }
        
        return ResponseEntity.ok(articles);
    }
    
    // 인기 게시글 조회
    @GetMapping("/popular")
    public ResponseEntity<List<ArticleDto>> getPopularArticles(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<ArticleDto> popularArticles = articleService.getPopularArticles(pageable);
        
        return ResponseEntity.ok(popularArticles);
    }
    
    // 최신 게시글 조회
    @GetMapping("/recent")
    public ResponseEntity<List<ArticleDto>> getRecentArticles(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<ArticleDto> recentArticles = articleService.getRecentArticles(pageable);
        
        return ResponseEntity.ok(recentArticles);
    }
    
    // 특정 사용자의 게시글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ArticleDto>> getArticlesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, 
                           Sort.by("createdAt").descending());
        
        Page<ArticleDto> articles = articleService.getArticlesByUser(userId, pageable);
        return ResponseEntity.ok(articles);
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