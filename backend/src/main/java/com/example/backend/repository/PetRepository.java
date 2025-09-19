package com.example.backend.repository;

import com.example.backend.entity.Pet;
import com.example.backend.entity.PetRarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    // 펫 이름으로 찾기
    Optional<Pet> findByName(String name);
    
    // 펫 이름 중복 체크
    boolean existsByName(String name);
    
    // 이름으로 검색 (페이징)
    Page<Pet> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // 등급별 조회
    Page<Pet> findByRarity(PetRarity rarity, Pageable pageable);
    
    // 능력 설명으로 검색
    Page<Pet> findByAbilityContainingIgnoreCase(String ability, Pageable pageable);
    
    // 복합 검색 (이름 또는 능력)
    Page<Pet> findByNameContainingIgnoreCaseOrAbilityContainingIgnoreCase(
        String name, String ability, Pageable pageable);
    
    // 출시일 순으로 정렬 (최신순)
    @Query("SELECT p FROM Pet p ORDER BY p.releaseDate DESC")
    List<Pet> findRecentPets(Pageable pageable);
    
    // 출시일 순으로 정렬 (오래된순)
    @Query("SELECT p FROM Pet p ORDER BY p.releaseDate ASC")
    List<Pet> findOldestPets(Pageable pageable);
    
    // 등급별 개수 조회
    @Query("SELECT COUNT(p) FROM Pet p WHERE p.rarity = :rarity")
    Long countByRarity(@Param("rarity") PetRarity rarity);
    
    // 쿠키가 있는 펫들 조회
    @Query("SELECT p FROM Pet p WHERE SIZE(p.cookies) > 0")
    Page<Pet> findPetsWithCookies(Pageable pageable);
    
    // 쿠키가 없는 펫들 조회
    @Query("SELECT p FROM Pet p WHERE SIZE(p.cookies) = 0")
    Page<Pet> findPetsWithoutCookies(Pageable pageable);
}