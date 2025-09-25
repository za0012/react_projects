package com.example.backend.dto;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserDto user;
    
    // 추가된 필드들 - 프론트엔드에서 권한 판단용
    private Set<String> roles;          // 사용자의 모든 역할
    private Set<String> permissions;    // 사용자의 모든 권한
    private boolean isAdmin;            // 관리자 여부
    private boolean isManager;          // 매니저 여부
    private String highestRole;         // 최고 권한 역할

    // 생성자
    public LoginResponseDto(String accessToken, String refreshToken, String tokenType, 
                           Long expiresIn, UserDto user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
        
        // user 객체에서 역할/권한 정보 추출
        if (user != null) {
            this.roles = user.getRoles();
            this.permissions = user.getPermissions();
            this.isAdmin = user.hasRole("ROLE_ADMIN");
            this.isManager = user.hasRole("ROLE_MANAGER");
            this.highestRole = determineHighestRole(user);
        }
    }
    
    // 최고 권한 역할 결정
    private String determineHighestRole(UserDto user) {
        if (user.hasRole("ROLE_ADMIN")) {
            return "ADMIN";
        } else if (user.hasRole("ROLE_MANAGER")) {
            return "MANAGER";
        } else if (user.hasRole("ROLE_USER")) {
            return "USER";
        }
        return "GUEST";
    }
    
    // 권한 체크 메서드들
    public boolean canManageCookies() {
        return permissions != null && (
            permissions.contains("MANAGE_COOKIES") || 
            permissions.contains("WRITE_COOKIES")
        );
    }
    
    public boolean canManageUsers() {
        return permissions != null && permissions.contains("MANAGE_USERS");
    }
    
    public boolean canAccessAdmin() {
        return permissions != null && permissions.contains("ADMIN_ACCESS");
    }
}