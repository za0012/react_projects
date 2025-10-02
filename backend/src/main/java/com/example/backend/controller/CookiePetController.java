package com.example.backend.controller;

import com.example.backend.dto.PetDto;
import com.example.backend.service.CookieService;
import com.example.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/cookies/{cookieId}/pets")
@CrossOrigin(origins = "*")
public class CookiePetController {
    
    @Autowired
    private CookieService cookieService;
    
    @Autowired
    private PetService petService;
    
    // 쿠키에 펫 추가 (펫 이름으로)
    @PostMapping("/add/{petName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> addPetByName(
            @PathVariable Long cookieId, 
            @PathVariable String petName) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            cookieService.addPetToCookie(cookieId, petName);
            response.put("success", true);
            response.put("message", "펫이 쿠키에 성공적으로 추가되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키에 여러 펫 추가 (펫 이름들로)
    @PostMapping("/add-multiple")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> addMultiplePetsByName(
            @PathVariable Long cookieId, 
            @RequestBody List<String> petNames) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            cookieService.addPetsToCookie(cookieId, petNames);
            response.put("success", true);
            response.put("message", "펫들이 쿠키에 성공적으로 추가되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키에서 펫 제거 (펫 이름으로)
    @DeleteMapping("/remove/{petName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> removePetByName(
            @PathVariable Long cookieId, 
            @PathVariable String petName) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            cookieService.removePetFromCookie(cookieId, petName);
            response.put("success", true);
            response.put("message", "펫이 쿠키에서 성공적으로 제거되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 쿠키의 모든 펫 조회
    @GetMapping
    public ResponseEntity<List<PetDto>> getCookiePets(@PathVariable Long cookieId) {
        try {
            List<PetDto> pets = cookieService.getCookiePets(cookieId);
            return ResponseEntity.ok(pets);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 펫 선택 옵션 조회 (모든 펫 목록)
    @GetMapping("/options")
    public ResponseEntity<List<Map<String, Object>>> getPetOptions() {
        // Pageable.unpaged()를 사용하여 모든 데이터 조회
        Page<PetDto> petPage = petService.getAllPets(Pageable.unpaged());
        
        List<Map<String, Object>> options = petPage.getContent().stream()
                .map(pet -> {
                    Map<String, Object> option = new HashMap<>();
                    option.put("id", pet.getId());
                    option.put("name", pet.getName());
                    option.put("rarity", pet.getRarity());
                    return option;
                })
                .toList();
        
        return ResponseEntity.ok(options);
    }
}