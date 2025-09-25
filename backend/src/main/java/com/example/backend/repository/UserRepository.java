package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 사용자명으로 사용자 찾기
    Optional<User> findByUsername(String username);
    
    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);
    
    // 사용자명 중복 체크
    boolean existsByUsername(String username);
    
    // 이메일 중복 체크
    boolean existsByEmail(String email);
    
    // 사용자명으로 검색 (페이징)
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    
    // 이메일로 검색 (페이징)
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    // 최근 가입한 사용자 조회
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers(Pageable pageable);
    
    // 특정 기간에 가입한 사용자 수 조회
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    Long countUsersByDateRange(@Param("startDate") LocalDateTime startDate, 
                              @Param("endDate") LocalDateTime endDate);
    
    // 게시글을 많이 작성한 사용자 조회
    @Query("SELECT u FROM User u LEFT JOIN u.articles a GROUP BY u ORDER BY COUNT(a) DESC")
    List<User> findTopUsersByArticleCount(Pageable pageable);
    
    // 댓글을 많이 작성한 사용자 조회
    @Query("SELECT u FROM User u LEFT JOIN u.comments c GROUP BY u ORDER BY COUNT(c) DESC")
    List<User> findTopUsersByCommentCount(Pageable pageable);
    
    // RBAC 관련 메서드들
    
    // 특정 역할을 가진 사용자들 조회
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    // 활성 사용자 수 조회
    Long countByEnabled(Boolean enabled);
    
    // 잠긴 계정 수 조회
    Long countByAccountNonLocked(Boolean accountNonLocked);
    
    // 최근 로그인한 사용자들 조회
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since ORDER BY u.lastLoginAt DESC")
    List<User> findRecentlyLoggedInUsers(@Param("since") LocalDateTime since, Pageable pageable);
    
    // 특정 권한을 가진 사용자들 조회
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r JOIN r.permissions p WHERE p.name = :permissionName")
    List<User> findByPermissionName(@Param("permissionName") String permissionName);
    
    // 관리자 사용자들 조회
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN ('ROLE_ADMIN', 'ROLE_MANAGER')")
    List<User> findAdministrators();
    
    // 일반 사용자들 조회
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_USER'")
    Page<User> findRegularUsers(Pageable pageable);
}