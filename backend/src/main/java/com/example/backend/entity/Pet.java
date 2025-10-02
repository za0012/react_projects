package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pets")
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name; // 펫 이름
    
    @Column(length = 500)
    private String imageUrl; // 이미지 URL
    
    @Column(length = 1000)
    private String ability; // 펫 능력 설명
    
    @Enumerated(EnumType.STRING)
    private PetRarity rarity; // 등급
    
    private LocalDate releaseDate; // 출시일
    
    @Column(length = 2000)
    private String description; // 펫 설명
    
    @ManyToMany(mappedBy = "pets", fetch = FetchType.LAZY)
    private Set<Cookie> cookies; // 이 펫을 가진 쿠키들
    
    // 생성자
    public Pet(String name, String imageUrl, String ability, PetRarity rarity, 
              LocalDate releaseDate, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.ability = ability;
        this.rarity = rarity;
        this.releaseDate = releaseDate;
        this.description = description;
    }
}