package com.example.backend.dto;

import com.example.backend.entity.Cookie;
import com.example.backend.entity.CookieRarity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
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

}