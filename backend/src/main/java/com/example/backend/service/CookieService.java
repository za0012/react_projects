package com.example.backend.service;

import com.example.backend.dto.CookieDto;
import com.example.backend.dto.PetDto;
import com.example.backend.entity.Cookie;
import com.example.backend.entity.CookieRarity;
import com.example.backend.entity.Pet;
import com.example.backend.repository.CookieRepository;
import com.example.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CookieService {
    
    @Autowired
    private CookieRepository cookieRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    // ==================== 기본 CRUD ====================
    
    // 모든 쿠키 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<CookieDto> getAllCookies(Pageable pageable) {
        return cookieRepository.findAll(pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 쿠키 상세 조회
    @Transactional(readOnly = true)
    public CookieDto getCookieById(Long id) {
        Cookie cookie = cookieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        return CookieDto.fromEntity(cookie);
    }
    
    // 쿠키 이름으로 조회
    @Transactional(readOnly = true)
    public CookieDto getCookieByName(String name) {
        Cookie cookie = cookieRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        return CookieDto.fromEntity(cookie);
    }
    
    // 쿠키 생성
    public CookieDto createCookie(CookieDto cookieDto) {
        // 이름 중복 체크
        if (cookieRepository.existsByName(cookieDto.getName())) {
            throw new RuntimeException("이미 존재하는 쿠키 이름입니다.");
        }
        
        Cookie cookie = cookieDto.toEntity();
        
        // 펫이 있는 경우 설정
        if (cookieDto.getPetIds() != null && !cookieDto.getPetIds().isEmpty()) {
            Set<Pet> pets = new HashSet<>(getPetsByIds(cookieDto.getPetIds()));
            cookie.setPets(pets);
        }
        
        Cookie savedCookie = cookieRepository.save(cookie);
        return CookieDto.fromEntity(savedCookie);
    }
    
    // 쿠키 수정
    public CookieDto updateCookie(Long id, CookieDto cookieDto) {
        Cookie cookie = cookieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        
        // 이름 변경 시 중복 체크
        if (!cookie.getName().equals(cookieDto.getName()) && 
            cookieRepository.existsByName(cookieDto.getName())) {
            throw new RuntimeException("이미 존재하는 쿠키 이름입니다.");
        }
        
        // 정보 업데이트
        cookie.setName(cookieDto.getName());
        cookie.setImageUrl(cookieDto.getImageUrl());
        cookie.setHealth(cookieDto.getHealth());
        cookie.setAbility(cookieDto.getAbility());
        cookie.setUnlockStarCandies(cookieDto.getUnlockStarCandies());
        cookie.setPartner(cookieDto.getPartner());
        cookie.setReleaseDate(cookieDto.getReleaseDate());
        cookie.setRarity(cookieDto.getRarity());
        cookie.setDescription(cookieDto.getDescription());
        
        // 펫 설정
        if (cookieDto.getPetIds() != null && !cookieDto.getPetIds().isEmpty()) {
            Set<Pet> pets = new HashSet<>(getPetsByIds(cookieDto.getPetIds()));
            cookie.setPets(pets);
        } else {
            cookie.setPets(new HashSet<>());
        }
        
        Cookie updatedCookie = cookieRepository.save(cookie);
        return CookieDto.fromEntity(updatedCookie);
    }
    
    // 쿠키 삭제
    public void deleteCookie(Long id) {
        Cookie cookie = cookieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        cookieRepository.delete(cookie);
    }
    
    // ==================== 검색 및 필터링 ====================
    
    // 이름으로 검색
    @Transactional(readOnly = true)
    public Page<CookieDto> searchByName(String name, Pageable pageable) {
        return cookieRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 등급별 조회
    @Transactional(readOnly = true)
    public Page<CookieDto> getCookiesByRarity(CookieRarity rarity, Pageable pageable) {
        return cookieRepository.findByRarity(rarity, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 체력 범위로 조회
    @Transactional(readOnly = true)
    public Page<CookieDto> getCookiesByHealthRange(Integer minHealth, Integer maxHealth, Pageable pageable) {
        return cookieRepository.findByHealthBetween(minHealth, maxHealth, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 체력 순 상위 쿠키 조회
    @Transactional(readOnly = true)
    public List<CookieDto> getTopCookiesByHealth(Pageable pageable) {
        return cookieRepository.findTopByHealthDesc(pageable)
                .stream()
                .map(CookieDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 해금별사탕수 범위로 조회
    @Transactional(readOnly = true)
    public Page<CookieDto> getCookiesByStarCandiesRange(Integer minCandies, Integer maxCandies, Pageable pageable) {
        return cookieRepository.findByUnlockStarCandiesBetween(minCandies, maxCandies, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 특정 펫을 가진 쿠키들 조회
    @Transactional(readOnly = true)
    public Page<CookieDto> getCookiesByPet(Long petId, Pageable pageable) {
        return cookieRepository.findByPetsId(petId, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 짝꿍이 있는 쿠키들 조회
    @Transactional(readOnly = true)
    public Page<CookieDto> getCookiesWithPartner(Pageable pageable) {
        return cookieRepository.findByPartnerIsNotNull(pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 짝꿍으로 검색
    @Transactional(readOnly = true)
    public Page<CookieDto> searchByPartner(String partner, Pageable pageable) {
        return cookieRepository.findByPartnerContainingIgnoreCase(partner, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 최신 쿠키 조회
    @Transactional(readOnly = true)
    public List<CookieDto> getRecentCookies(Pageable pageable) {
        return cookieRepository.findRecentCookies(pageable)
                .stream()
                .map(CookieDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 오래된 쿠키 조회
    @Transactional(readOnly = true)
    public List<CookieDto> getOldestCookies(Pageable pageable) {
        return cookieRepository.findOldestCookies(pageable)
                .stream()
                .map(CookieDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 능력으로 검색
    @Transactional(readOnly = true)
    public Page<CookieDto> searchByAbility(String ability, Pageable pageable) {
        return cookieRepository.findByAbilityContainingIgnoreCase(ability, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 복합 검색 (이름 또는 능력)
    @Transactional(readOnly = true)
    public Page<CookieDto> searchByNameOrAbility(String keyword, Pageable pageable) {
        return cookieRepository.findByNameContainingIgnoreCaseOrAbilityContainingIgnoreCase(
                keyword, keyword, pageable)
                .map(CookieDto::fromEntity);
    }
    
    // 이름 중복 체크
    @Transactional(readOnly = true)
    public boolean isNameExists(String name) {
        return cookieRepository.existsByName(name);
    }
    
    // ==================== 통계 ====================
    
    // 통계 정보 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getCookieStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 쿠키 수
        stats.put("totalCount", cookieRepository.count());
        
        // 등급별 개수
        Map<String, Long> rarityStats = new HashMap<>();
        for (CookieRarity rarity : CookieRarity.values()) {
            rarityStats.put(rarity.name(), cookieRepository.countByRarity(rarity));
        }
        stats.put("rarityStats", rarityStats);
        
        // 평균 체력
        stats.put("averageHealth", cookieRepository.getAverageHealth());
        
        // 평균 해금별사탕수
        stats.put("averageStarCandies", cookieRepository.getAverageUnlockStarCandies());
        
        return stats;
    }
    
    // ==================== 펫 관련 메서드 ====================
    
    // 펫 이름으로 쿠키에 펫 추가
    public void addPetToCookie(Long cookieId, String petName) {
        Cookie cookie = cookieRepository.findById(cookieId)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        
        Pet pet = petRepository.findByName(petName)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다: " + petName));
        
        cookie.addPet(pet);
        cookieRepository.save(cookie);
    }
    
    // 여러 펫을 한번에 추가 (펫 이름들로)
    public void addPetsToCookie(Long cookieId, List<String> petNames) {
        Cookie cookie = cookieRepository.findById(cookieId)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        
        for (String petName : petNames) {
            Pet pet = petRepository.findByName(petName)
                    .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다: " + petName));
            cookie.addPet(pet);
        }
        
        cookieRepository.save(cookie);
    }
    
    // 쿠키에서 펫 제거 (펫 이름으로)
    public void removePetFromCookie(Long cookieId, String petName) {
        Cookie cookie = cookieRepository.findById(cookieId)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        
        Pet pet = petRepository.findByName(petName)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다: " + petName));
        
        cookie.removePet(pet);
        cookieRepository.save(cookie);
    }
    
    // 쿠키의 모든 펫 조회
    @Transactional(readOnly = true)
    public List<PetDto> getCookiePets(Long cookieId) {
        Cookie cookie = cookieRepository.findById(cookieId)
                .orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 없습니다."));
        
        return cookie.getPets().stream()
                .map(PetDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // ==================== 헬퍼 메서드 ====================
    
    // Pet ID 목록으로 Pet 엔티티 조회
    private List<Pet> getPetsByIds(List<Long> petIds) {
        return petIds.stream()
                .map(petId -> petRepository.findById(petId)
                        .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다: ID " + petId)))
                .collect(Collectors.toList());
    }
}