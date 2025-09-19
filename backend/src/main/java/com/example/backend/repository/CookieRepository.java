package com.example.backend.repository;

import com.example.backend.entity.Cookie;
import com.example.backend.entity.CookieRarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CookieRepository extends JpaRepository<Cookie, Long> {
    
    // 쿠키 이름으로 찾기
    Optional<Cookie> findByName(String name);
    
    // 쿠키 이름 중복 체크
    boolean existsByName(String name);
    
    // 이름으로 검색 (페이징)
    Page<Cookie> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // 등급별 조회
    Page<Cookie> findByRarity(CookieRarity rarity, Pageable pageable);
    
    // 체력 범위로 조회
    Page<Cookie> findByHealthBetween(Integer minHealth, Integer maxHealth, Pageable pageable);
    
    // 체력 순으로 정렬 조회
    @Query("SELECT c FROM Cookie c ORDER BY c.health DESC")
    List<Cookie> findTopByHealthDesc(Pageable pageable);
    
    // 해금별사탕수 범위로 조회
    Page<Cookie> findByUnlockStarCandiesBetween(Integer minCandies, Integer maxCandies, Pageable pageable);
    
    // 특정 펫을 가진 쿠키들 조회
    Page<Cookie> findByPetId(Long petId, Pageable pageable);
    
    // 짝꿍이 있는 쿠키들 조회
    Page<Cookie> findByPartnerIsNotNull(Pageable pageable);
    
    // 특정 짝꿍을 가진 쿠키들 조회
    Page<Cookie> findByPartnerContainingIgnoreCase(String partner, Pageable pageable);
    
    // 출시일 순으로 정렬 (최신순)
    @Query("SELECT c FROM Cookie c ORDER BY c.releaseDate DESC")
    List<Cookie> findRecentCookies(Pageable pageable);
    
    // 출시일 순으로 정렬 (오래된순)
    @Query("SELECT c FROM Cookie c ORDER BY c.releaseDate ASC")
    List<Cookie> findOldestCookies(Pageable pageable);
    
    // 능력 설명으로 검색
    Page<Cookie> findByAbilityContainingIgnoreCase(String ability, Pageable pageable);
    
    // 복합 검색 (이름 또는 능력)
    Page<Cookie> findByNameContainingIgnoreCaseOrAbilityContainingIgnoreCase(
        String name, String ability, Pageable pageable);
    
    // 등급별 개수 조회
    @Query("SELECT COUNT(c) FROM Cookie c WHERE c.rarity = :rarity")
    Long countByRarity(@Param("rarity") CookieRarity rarity);
    
    // 체력 평균 조회
    @Query("SELECT AVG(c.health) FROM Cookie c")
    Double getAverageHealth();
    
    // 해금별사탕수 평균 조회
    @Query("SELECT AVG(c.unlockStarCandies) FROM Cookie c")
    Double getAverageUnlockStarCandies();
}