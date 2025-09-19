package com.example.backend.controller;

import com.example.backend.dto.PetDto;
import com.example.backend.entity.PetRarity;
import com.example.backend.service.PetService;
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
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {
    
    @Autowired
    private PetService petService;
    
    // 모든 펫 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<PetDto>> getAllPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PetDto> pets = petService.getAllPets(pageable);
        
        return ResponseEntity.ok(pets);
    }
    
    // 펫 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        try {
            PetDto pet = petService.getPetById(id);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 펫 이름으로 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<PetDto> getPetByName(@PathVariable String name) {
        try {
            PetDto pet = petService.getPetByName(name);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 펫 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPet(@Valid @RequestBody PetDto petDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            PetDto createdPet = petService.createPet(petDto);
            response.put("success", true);
            response.put("message", "펫이 성공적으로 생성되었습니다.");
            response.put("data", createdPet);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 펫 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePet(
            @PathVariable Long id, 
            @Valid @RequestBody PetDto petDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            PetDto updatedPet = petService.updatePet(id, petDto);
            response.put("success", true);
            response.put("message", "펫이 성공적으로 수정되었습니다.");
            response.put("data", updatedPet);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 펫 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePet(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            petService.deletePet(id);
            response.put("success", true);
            response.put("message", "펫이 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 펫 검색
    @GetMapping("/search")
    public ResponseEntity<Page<PetDto>> searchPets(
            @RequestParam(required = false) String type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        
        Page<PetDto> pets;
        
        if (type == null || type.equals("all")) {
            pets = petService.searchByNameOrAbility(keyword, pageable);
        } else if (type.equals("name")) {
            pets = petService.searchByName(keyword, pageable);
        } else if (type.equals("ability")) {
            pets = petService.searchByAbility(keyword, pageable);
        } else {
            pets = petService.searchByNameOrAbility(keyword, pageable);
        }
        
        return ResponseEntity.ok(pets);
    }
    
    // 등급별 펫 조회
    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<Page<PetDto>> getPetsByRarity(
            @PathVariable String rarity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            PetRarity petRarity = PetRarity.valueOf(rarity.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<PetDto> pets = petService.getPetsByRarity(petRarity, pageable);
            
            return ResponseEntity.ok(pets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 쿠키가 있는 펫들 조회
    @GetMapping("/with-cookies")
    public ResponseEntity<Page<PetDto>> getPetsWithCookies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<PetDto> pets = petService.getPetsWithCookies(pageable);
        
        return ResponseEntity.ok(pets);
    }
    
    // 쿠키가 없는 펫들 조회
    @GetMapping("/without-cookies")
    public ResponseEntity<Page<PetDto>> getPetsWithoutCookies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<PetDto> pets = petService.getPetsWithoutCookies(pageable);
        
        return ResponseEntity.ok(pets);
    }
    
    // 최신 펫 조회
    @GetMapping("/recent")
    public ResponseEntity<List<PetDto>> getRecentPets(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<PetDto> recentPets = petService.getRecentPets(pageable);
        
        return ResponseEntity.ok(recentPets);
    }
    
    // 펫 이름 중복 체크
    @GetMapping("/check/name/{name}")
    public ResponseEntity<Map<String, Object>> checkNameExists(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = petService.isNameExists(name);
        
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 펫 이름입니다." : "사용 가능한 펫 이름입니다.");
        
        return ResponseEntity.ok(response);
    }
    
    // 펫 통계 조회
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPetStatistics() {
        Map<String, Object> statistics = petService.getPetStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    // 등급 목록 조회
    @GetMapping("/rarities")
    public ResponseEntity<Map<String, String>> getRarities() {
        Map<String, String> rarities = new HashMap<>();
        for (PetRarity rarity : PetRarity.values()) {
            rarities.put(rarity.name(), rarity.getKoreanName());
        }
        return ResponseEntity.ok(rarities);
    }
}