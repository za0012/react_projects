package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet; // 펫

    private LocalDate releaseDate; // 출시일

    @Enumerated(EnumType.STRING)
    private CookieRarity rarity; // 등급 (커먼, 레어, 에픽, 레전더리 등)

    @Column(length = 2000)
    private String description; // 쿠키 설명
}