package com.example.backend.service;

import com.example.backend.dto.ArticleDto;
import com.example.backend.entity.Article;
import com.example.backend.entity.User;
import com.example.backend.repository.ArticleRepository;
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
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 모든 게시글 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<ArticleDto> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 게시글 상세 조회 (조회수 증가)
    @Transactional
    public ArticleDto getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        article.increaseViewCount();
        articleRepository.save(article);
        
        return ArticleDto.fromEntity(article);
    }
    
    // 게시글 상세 조회 (조회수 증가 없음)
    @Transactional(readOnly = true)
    public ArticleDto getArticleByIdWithoutView(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return ArticleDto.fromEntity(article);
    }
    
    // 게시글 생성
    public ArticleDto createArticle(ArticleDto articleDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Article article = articleDto.toEntity(username, user);
        Article savedArticle = articleRepository.save(article);
        
        return ArticleDto.fromEntity(savedArticle);
    }
    
    // 게시글 수정
    public ArticleDto updateArticle(Long id, ArticleDto articleDto, String username) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자 확인
        if (article.getUser() == null || !article.getUser().getUsername().equals(username)) {
            throw new RuntimeException("게시글을 수정할 권한이 없습니다.");
        }

        article.update(articleDto.getTitle(), articleDto.getContent());
        Article updatedArticle = articleRepository.save(article);
        
        return ArticleDto.fromEntity(updatedArticle);
    }
    
    // 게시글 삭제
    public void deleteArticle(Long id, String username) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 작성자 확인
        if (article.getUser() == null || !article.getUser().getUsername().equals(username)) {
            throw new RuntimeException("게시글을 삭제할 권한이 없습니다.");
        }
        
        articleRepository.delete(article);
    }
    
    // 제목으로 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchByTitle(String title, Pageable pageable) {
        return articleRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 내용으로 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchByContent(String content, Pageable pageable) {
        return articleRepository.findByContentContainingIgnoreCase(content, pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 작성자로 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchByUsername(String author, Pageable pageable) {
        return articleRepository.findByUser_UsernameContainingIgnoreCase(author, pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 제목 또는 내용으로 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchByTitleOrContent(String keyword, Pageable pageable) {
        return articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword, keyword, pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 특정 사용자의 게시글 조회
    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticlesByUser(Long userId, Pageable pageable) {
        return articleRepository.findByUserId(userId, pageable)
                .map(ArticleDto::fromEntity);
    }
    
    // 인기 게시글 조회 (조회수 기준)
    @Transactional(readOnly = true)
    public List<ArticleDto> getPopularArticles(Pageable pageable) {
        return articleRepository.findTopByViewCount(pageable)
                .stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 최신 게시글 조회
    @Transactional(readOnly = true)
    public List<ArticleDto> getRecentArticles(Pageable pageable) {
        return articleRepository.findRecentArticles(pageable)
                .stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

}