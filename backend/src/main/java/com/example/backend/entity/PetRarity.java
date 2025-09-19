package com.example.backend.entity;

public enum PetRarity {
    COMMON("커먼", "#FFFFFF"),
    RARE("레어", "#00FF00"),
    EPIC("에픽", "#800080"),
    LEGENDARY("레전더리", "#FFD700");
    
    private final String koreanName;
    private final String colorCode;
    
    PetRarity(String koreanName, String colorCode) {
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