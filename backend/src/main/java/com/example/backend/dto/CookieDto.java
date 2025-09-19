package com.example.backend.dto;

import com.example.backend.entity.Cookie;
import com.example.backend.entity.CookieRarity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CookieDto {
    private Long id;
    
    @NotBlank(message = "쿠키 이름은 필수입니다.")
    private String name;
    
    private String imageUrl;
    
    @NotNull(message = "체력은 필수입니다.")
    @Min(value = 1, message = "체력은 1 이상이어야 합니다.")
    private Integer health;
    
    private String ability;
    
    @NotNull(message = "해금별사탕수는 필수입니다.")
    @Min(value = 0, message = "해금별사탕수는 0 이상이어야 합니다.")
    private Integer unlockStarCandies;
    
    private String partner;
    private Long petId;
    private String petName; // 조회용
    private LocalDate releaseDate;
    private CookieRarity rarity;
    private String description;
    
    // 기본 생성자
    public CookieDto() {}
    
    // 전체 생성자
    public CookieDto(Long id, String name, String imageUrl, Integer health, String ability,
                    Integer unlockStarCandies, String partner, Long petId, String petName,
                    LocalDate releaseDate, CookieRarity rarity, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.health = health;
        this.ability = ability;
        this.unlockStarCandies = unlockStarCandies;
        this.partner = partner;
        this.petId = petId;
        this.petName = petName;
        this.releaseDate = releaseDate;
        this.rarity = rarity;
        this.description = description;
    }
    
    // Entity -> DTO 변환
    public static CookieDto fromEntity(Cookie cookie) {
        return new CookieDto(
            cookie.getId(),
            cookie.getName(),
            cookie.getImageUrl(),
            cookie.getHealth(),
            cookie.getAbility(),
            cookie.getUnlockStarCandies(),
            cookie.getPartner(),
            cookie.getPet() != null ? cookie.getPet().getId() : null,
            cookie.getPet() != null ? cookie.getPet().getName() : null,
            cookie.getReleaseDate(),
            cookie.getRarity(),
            cookie.getDescription()
        );
    }
    
    // DTO -> Entity 변환 (새 쿠키 생성용)
    public Cookie toEntity() {
        Cookie cookie = new Cookie();
        cookie.setName(this.name);
        cookie.setImageUrl(this.imageUrl);
        cookie.setHealth(this.health);
        cookie.setAbility(this.ability);
        cookie.setUnlockStarCandies(this.unlockStarCandies);
        cookie.setPartner(this.partner);
        cookie.setReleaseDate(this.releaseDate);
        cookie.setRarity(this.rarity);
        cookie.setDescription(this.description);
        return cookie;
    }
    
    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getHealth() { return health; }
    public void setHealth(Integer health) { this.health = health; }
    
    public String getAbility() { return ability; }
    public void setAbility(String ability) { this.ability = ability; }
    
    public Integer getUnlockStarCandies() { return unlockStarCandies; }
    public void setUnlockStarCandies(Integer unlockStarCandies) { this.unlockStarCandies = unlockStarCandies; }
    
    public String getPartner() { return partner; }
    public void setPartner(String partner) { this.partner = partner; }
    
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
    
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    
    public CookieRarity getRarity() { return rarity; }
    public void setRarity(CookieRarity rarity) { this.rarity = rarity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}