package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
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
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cookie> cookies; // 이 펫을 가진 쿠키들
    
    // 기본 생성자
    public Pet() {}
    
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
    
    public List<Cookie> getCookies() { return cookies; }
    public void setCookies(List<Cookie> cookies) { this.cookies = cookies; }
}