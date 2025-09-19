package com.example.backend.controller;

import com.example.backend.dto.CookieDto;
import com.example.backend.entity.CookieRarity;
import com.example.backend.service.CookieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cookies")
@CrossOrigin(origins = "*")
public class CookieController {
    
    @Autowired
    private CookieService cookieService;
    
    // 모든 쿠키 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<CookieDto>> getAllCookies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CookieDto> cookies = cookieService.getAllCookies(pageable);
        
        return ResponseEntity.ok(cookies);
    }
    
    // 쿠키 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<CookieDto> getCookieById(@PathVariable Long id) {
        try {
            CookieDto cookie = cookieService.getCookieById(id);
            return ResponseEntity.ok(cookie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 쿠키 이름으로 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<CookieDto> getCookieByName(@PathVariable String name) {
        try {
            CookieDto cookie = cookieService.getCookieByName(name);
            return ResponseEntity.ok(cookie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 쿠키 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCookie(@Valid @RequestBody CookieDto cookieDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            CookieDto createdCookie = cookieService.createCookie(cookieDto);
            response.put("success", true);
            response.put("message", "쿠키가 성공적으로 생성되었습니다.");
            response.put("data", createdCookie);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCookie(
            @PathVariable Long id, 
            @Valid @RequestBody CookieDto cookieDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            CookieDto updatedCookie = cookieService.updateCookie(id, cookieDto);
            response.put("success", true);
            response.put("message", "쿠키가 성공적으로 수정되었습니다.");
            response.put("data", updatedCookie);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCookie(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            cookieService.deleteCookie(id);
            response.put("success", true);
            response.put("message", "쿠키가 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키 검색
    @GetMapping("/search")
    public ResponseEntity<Page<CookieDto>> searchCookies(
            @RequestParam(required = false) String type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        
        Page<CookieDto> cookies;
        
        if (type == null || type.equals("all")) {
            cookies = cookieService.searchByNameOrAbility(keyword, pageable);
        } else if (type.equals("name")) {
            cookies = cookieService.searchByName(keyword, pageable);
        } else if (type.equals("ability")) {
            cookies = cookieService.searchByAbility(keyword, pageable);
        } else if (type.equals("partner")) {
            cookies = cookieService.searchByPartner(keyword, pageable);
        } else {
            cookies = cookieService.searchByNameOrAbility(keyword, pageable);
        }
        
        return ResponseEntity.ok(cookies);
    }
    
    // 등급별 쿠키 조회
    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<Page<CookieDto>> getCookiesByRarity(
            @PathVariable String rarity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            CookieRarity cookieRarity = CookieRarity.valueOf(rarity.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<CookieDto> cookies = cookieService.getCookiesByRarity(cookieRarity, pageable);
            
            return ResponseEntity.ok(cookies);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 체력 범위로 쿠키 조회
    @GetMapping("/health")
    public ResponseEntity<Page<CookieDto>> getCookiesByHealthRange(
            @RequestParam Integer minHealth,
            @RequestParam Integer maxHealth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("health").descending());
        Page<CookieDto> cookies = cookieService.getCookiesByHealthRange(minHealth, maxHealth, pageable);
        
        return ResponseEntity.ok(cookies);
    }
    
    // 체력 순 상위 쿠키 조회
    @GetMapping("/top/health")
    public ResponseEntity<List<CookieDto>> getTopCookiesByHealth(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<CookieDto> topCookies = cookieService.getTopCookiesByHealth(pageable);
        
        return ResponseEntity.ok(topCookies);
    }
    
    // 해금별사탕수 범위로 쿠키 조회
    @GetMapping("/star-candies")
    public ResponseEntity<Page<CookieDto>> getCookiesByStarCandiesRange(
            @RequestParam Integer minCandies,
            @RequestParam Integer maxCandies,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("unlockStarCandies").ascending());
        Page<CookieDto> cookies = cookieService.getCookiesByStarCandiesRange(minCandies, maxCandies, pageable);
        
        return ResponseEntity.ok(cookies);
    }
    
    // 특정 펫을 가진 쿠키들 조회
    @GetMapping("/pet/{petId}")
    public ResponseEntity<Page<CookieDto>> getCookiesByPet(
            @PathVariable Long petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CookieDto> cookies = cookieService.getCookiesByPet(petId, pageable);
        
        return ResponseEntity.ok(cookies);
    }
    
    // 짝꿍이 있는 쿠키들 조회
    @GetMapping("/with-partner")
    public ResponseEntity<Page<CookieDto>> getCookiesWithPartner(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CookieDto> cookies = cookieService.getCookiesWithPartner(pageable);
        
        return ResponseEntity.ok(cookies);
    }
    
    // 최신 쿠키 조회
    @GetMapping("/recent")
    public ResponseEntity<List<CookieDto>> getRecentCookies(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<CookieDto> recentCookies = cookieService.getRecentCookies(pageable);
        
        return ResponseEntity.ok(recentCookies);
    }
    
    // 쿠키 이름 중복 체크
    @GetMapping("/check/name/{name}")
    public ResponseEntity<Map<String, Object>> checkNameExists(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = cookieService.isNameExists(name);
        
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 쿠키 이름입니다." : "사용 가능한 쿠키 이름입니다.");
        
        return ResponseEntity.ok(response);
    }
    
    // 쿠키 통계 조회
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCookieStatistics() {
        Map<String, Object> statistics = cookieService.getCookieStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    // 등급 목록 조회
    @GetMapping("/rarities")
    public ResponseEntity<Map<String, String>> getRarities() {
        Map<String, String> rarities = new HashMap<>();
        for (CookieRarity rarity : CookieRarity.values()) {
            rarities.put(rarity.name(), rarity.getKoreanName());
        }
        return ResponseEntity.ok(rarities);
    }
}