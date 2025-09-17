package com.example.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    // JWT 비밀키 (환경변수나 application.yml에서 관리하는 것이 좋습니다)
    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;
    
    // Access Token 유효시간 (1시간)
    @Value("${jwt.access-token-validity:3600}")
    private Long accessTokenValidity;
    
    // Refresh Token 유효시간 (7일)
    @Value("${jwt.refresh-token-validity:604800}")
    private Long refreshTokenValidity;
    
    // 비밀키 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    // 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // 토큰에서 만료일 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // 토큰에서 특정 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // 토큰에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // 토큰 만료 확인
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    // Access Token 생성
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, username, accessTokenValidity * 1000);
    }
    
    // Refresh Token 생성
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, username, refreshTokenValidity * 1000);
    }
    
    // 토큰 생성
    private String createToken(Map<String, Object> claims, String subject, Long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    // 토큰 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    // 토큰 검증 (사용자명만으로)
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
    
    // 토큰 타입 확인
    public String getTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("type");
    }
    
    // Refresh Token인지 확인
    public Boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }
    
    // Access Token인지 확인
    public Boolean isAccessToken(String token) {
        try {
            return "access".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }
}