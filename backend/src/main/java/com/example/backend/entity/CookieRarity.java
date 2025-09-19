package com.example.backend.entity;

public enum CookieRarity {
    COMMON("커먼", "#FFFFFF"),
    RARE("레어", "#00FF00"),
    EPIC("에픽", "#800080"),
    LEGENDARY("레전더리", "#FFD700"),
    SPECIAL("스페셜", "#FF69B4");
    
    private final String koreanName;
    private final String colorCode;
    
    CookieRarity(String koreanName, String colorCode) {
        this.koreanName = koreanName;
        this.colorCode = colorCode;
    }
    
    public String getKoreanName() {
        return koreanName;
    }
    
    public String getColorCode() {
        return colorCode;
    }
}