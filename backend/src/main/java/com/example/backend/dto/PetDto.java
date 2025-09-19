package com.example.backend.dto;

import com.example.backend.entity.Pet;
import com.example.backend.entity.PetRarity;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public class PetDto {
    private Long id;
    
    @NotBlank(message = "펫 이름은 필수입니다.")
    private String name;
    
    private String imageUrl;
    private String ability;
    private PetRarity rarity;
    private LocalDate releaseDate;
    private String description;
    private List<String> cookieNames; // 이 펫을 가진 쿠키들의 이름
    
    // 기본 생성자
    public PetDto() {}
    
    // 전체 생성자
    public PetDto(Long id, String name, String imageUrl, String ability, PetRarity rarity,
                 LocalDate releaseDate, String description, List<String> cookieNames) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ability = ability;
        this.rarity = rarity;
        this.releaseDate = releaseDate;
        this.description = description;
        this.cookieNames = cookieNames;
    }
    
    // Entity -> DTO 변환
    public static PetDto fromEntity(Pet pet) {
        List<String> cookieNames = null;
        if (pet.getCookies() != null) {
            cookieNames = pet.getCookies().stream()
                    .map(cookie -> cookie.getName())
                    .toList();
        }
        
        return new PetDto(
            pet.getId(),
            pet.getName(),
            pet.getImageUrl(),
            pet.getAbility(),
            pet.getRarity(),
            pet.getReleaseDate(),
            pet.getDescription(),
            cookieNames
        );
    }
    
    // DTO -> Entity 변환
    public Pet toEntity() {
        Pet pet = new Pet();
        pet.setName(this.name);
        pet.setImageUrl(this.imageUrl);
        pet.setAbility(this.ability);
        pet.setRarity(this.rarity);
        pet.setReleaseDate(this.releaseDate);
        pet.setDescription(this.description);
        return pet;
    }
    
    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getAbility() { return ability; }
    public void setAbility(String ability) { this.ability = ability; }
    
    public PetRarity getRarity() { return rarity; }
    public void setRarity(PetRarity rarity) { this.rarity = rarity; }
    
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getCookieNames() { return cookieNames; }
    public void setCookieNames(List<String> cookieNames) { this.cookieNames = cookieNames; }
}