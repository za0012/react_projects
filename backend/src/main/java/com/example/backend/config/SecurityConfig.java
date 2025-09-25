package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션 활성화
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화
            .csrf(csrf -> csrf.disable())
            
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 세션 정책 - STATELESS (JWT 사용)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 인증 없이 접근 가능한 엔드포인트
                .requestMatchers("/api/users/register", "/api/users/login", "/api/users/refresh").permitAll()
                
                // 조회 기능은 누구나 접근 가능
                .requestMatchers("GET", "/api/articles/**").permitAll()
                .requestMatchers("GET", "/api/comments/**").permitAll()
                .requestMatchers("GET", "/api/users/check/**").permitAll()
                .requestMatchers("GET", "/api/cookies/**").permitAll()
                .requestMatchers("GET", "/api/pets/**").permitAll()

                // 관리자 API는 관리자만 접근 가능
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // 쿠키/펫 생성/수정/삭제는 MANAGER 이상
                .requestMatchers("POST", "/api/cookies/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("PUT", "/api/cookies/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("DELETE", "/api/cookies/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("POST", "/api/pets/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("PUT", "/api/pets/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("DELETE", "/api/pets/**").hasAnyRole("ADMIN", "MANAGER")
                
                // 게시글/댓글 작성은 인증된 사용자
                .requestMatchers("POST", "/api/articles/**").authenticated()
                .requestMatchers("POST", "/api/comments/**").authenticated()
                
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            
            // JWT 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // HTTP Basic 인증 비활성화
            .httpBasic(basic -> basic.disable())
            
            // Form 로그인 비활성화
            .formLogin(form -> form.disable());
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 모든 출처 허용 (개발 환경용)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // 인증 정보 허용
        configuration.setAllowCredentials(true);
        
        // 프리플라이트 요청 캐시 시간
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}