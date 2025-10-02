package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 전용으로만 사용
@Builder
@Table(name = "cookies")
public class Cookie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 쿠키 이름

    @Column(length = 500)
    private String imageUrl; // 이미지 URL

    @Column(nullable = false)
    private Integer health; // 체력

    @Column(length = 1000)
    private String ability; // 능력 설명

    @Column(nullable = false)
    private Integer unlockStarCandies; // 해금별사탕수

    private String partner; // 짝꿍

    // 변경: 다대다 관계로 여러 펫 연결 가능
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cookie_pets",
        joinColumns = @JoinColumn(name = "cookie_id"),
        inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    private Set<Pet> pets; // 여러 펫들

    private LocalDate releaseDate; // 출시일

    @Enumerated(EnumType.STRING)
    private CookieRarity rarity; // 등급 (커먼, 레어, 에픽, 레전더리 등)

    @Column(length = 2000)
    private String description; // 쿠키 설명

    public Cookie(String name, String imageUrl, Integer health, String ability, 
                 Integer unlockStarCandies, String partner, Set<Pet> pets, 
                 LocalDate releaseDate, CookieRarity rarity, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.health = health;
        this.ability = ability;
        this.unlockStarCandies = unlockStarCandies;
        this.partner = partner;
        this.pets = pets;
        this.releaseDate = releaseDate;
        this.rarity = rarity;
        this.description = description;
    }
    
    // 펫 관리 편의 메서드들
    public void addPet(Pet pet) {
        if (pets != null) {
            pets.add(pet);
        }
    }
    
    public void removePet(Pet pet) {
        if (pets != null) {
            pets.remove(pet);
        }
    }
}