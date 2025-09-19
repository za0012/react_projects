package com.example.backend.service;

import com.example.backend.dto.PetDto;
import com.example.backend.entity.Pet;
import com.example.backend.entity.PetRarity;
import com.example.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {
    
    @Autowired
    private PetRepository petRepository;
    
    // 모든 펫 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<PetDto> getAllPets(Pageable pageable) {
        return petRepository.findAll(pageable)
                .map(PetDto::fromEntity);
    }
    
    // 펫 상세 조회
    @Transactional(readOnly = true)
    public PetDto getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다."));
        return PetDto.fromEntity(pet);
    }
    
    // 펫 이름으로 조회
    @Transactional(readOnly = true)
    public PetDto getPetByName(String name) {
        Pet pet = petRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다."));
        return PetDto.fromEntity(pet);
    }
    
    // 펫 생성
    public PetDto createPet(PetDto petDto) {
        // 이름 중복 체크
        if (petRepository.existsByName(petDto.getName())) {
            throw new RuntimeException("이미 존재하는 펫 이름입니다.");
        }
        
        Pet pet = petDto.toEntity();
        Pet savedPet = petRepository.save(pet);
        
        return PetDto.fromEntity(savedPet);
    }
    
    // 펫 수정
    public PetDto updatePet(Long id, PetDto petDto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다."));
        
        // 이름 변경 시 중복 체크
        if (!pet.getName().equals(petDto.getName()) && 
            petRepository.existsByName(petDto.getName())) {
            throw new RuntimeException("이미 존재하는 펫 이름입니다.");
        }
        
        // 정보 업데이트
        pet.setName(petDto.getName());
        pet.setImageUrl(petDto.getImageUrl());
        pet.setAbility(petDto.getAbility());
        pet.setRarity(petDto.getRarity());
        pet.setReleaseDate(petDto.getReleaseDate());
        pet.setDescription(petDto.getDescription());
        
        Pet updatedPet = petRepository.save(pet);
        return PetDto.fromEntity(updatedPet);
    }
    
    // 펫 삭제
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다."));
        
        // 연관된 쿠키가 있는지 확인
        if (pet.getCookies() != null && !pet.getCookies().isEmpty()) {
            throw new RuntimeException("이 펫을 사용하는 쿠키가 있어서 삭제할 수 없습니다.");
        }
        
        petRepository.delete(pet);
    }
    
    // 이름으로 검색
    @Transactional(readOnly = true)
    public Page<PetDto> searchByName(String name, Pageable pageable) {
        return petRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(PetDto::fromEntity);
    }
    
    // 등급별 조회
    @Transactional(readOnly = true)
    public Page<PetDto> getPetsByRarity(PetRarity rarity, Pageable pageable) {
        return petRepository.findByRarity(rarity, pageable)
                .map(PetDto::fromEntity);
    }
    
    // 능력으로 검색
    @Transactional(readOnly = true)
    public Page<PetDto> searchByAbility(String ability, Pageable pageable) {
        return petRepository.findByAbilityContainingIgnoreCase(ability, pageable)
                .map(PetDto::fromEntity);
    }
    
    // 복합 검색 (이름 또는 능력)
    @Transactional(readOnly = true)
    public Page<PetDto> searchByNameOrAbility(String keyword, Pageable pageable) {
        return petRepository.findByNameContainingIgnoreCaseOrAbilityContainingIgnoreCase(
                keyword, keyword, pageable)
                .map(PetDto::fromEntity);
    }
    
    // 최신 펫 조회
    @Transactional(readOnly = true)
    public List<PetDto> getRecentPets(Pageable pageable) {
        return petRepository.findRecentPets(pageable)
                .stream()
                .map(PetDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 오래된 펫 조회
    @Transactional(readOnly = true)
    public List<PetDto> getOldestPets(Pageable pageable) {
        return petRepository.findOldestPets(pageable)
                .stream()
                .map(PetDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 쿠키가 있는 펫들 조회
    @Transactional(readOnly = true)
    public Page<PetDto> getPetsWithCookies(Pageable pageable) {
        return petRepository.findPetsWithCookies(pageable)
                .map(PetDto::fromEntity);
    }
    
    // 쿠키가 없는 펫들 조회
    @Transactional(readOnly = true)
    public Page<PetDto> getPetsWithoutCookies(Pageable pageable) {
        return petRepository.findPetsWithoutCookies(pageable)
                .map(PetDto::fromEntity);
    }
    
    // 이름 중복 체크
    @Transactional(readOnly = true)
    public boolean isNameExists(String name) {
        return petRepository.existsByName(name);
    }
    
    // 통계 정보 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getPetStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 펫 수
        stats.put("totalCount", petRepository.count());
        
        // 등급별 개수
        Map<String, Long> rarityStats = new HashMap<>();
        for (PetRarity rarity : PetRarity.values()) {
            rarityStats.put(rarity.name(), petRepository.countByRarity(rarity));
        }
        stats.put("rarityStats", rarityStats);
        
        return stats;
    }
}