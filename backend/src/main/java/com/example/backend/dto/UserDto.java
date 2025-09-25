package com.example.backend.dto;

import com.example.backend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
public class UserDto {
    private Long id;
    
    @NotBlank(message = "사용자명은 필수입니다.")
    @Size(min = 3, max = 20, message = "사용자명은 3-20자 사이로 입력하세요.")
    private String username;
    
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
    
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    
    // RBAC 관련 필드
    private Set<String> roles;
    private Set<String> permissions;
    
    // 기본 생성자
    public UserDto() {}
    
    // 생성자 (비밀번호 제외 - 응답용)
    public UserDto(Long id, String username, String email, Boolean enabled, 
                   Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired,
                   LocalDateTime createdAt, LocalDateTime lastLoginAt, 
                   Set<String> roles, Set<String> permissions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.roles = roles;
        this.permissions = permissions;
    }
    
    // 생성자 (전체)
    public UserDto(Long id, String username, String password, String email, Boolean enabled,
                   Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired,
                   LocalDateTime createdAt, LocalDateTime lastLoginAt,
                   Set<String> roles, Set<String> permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.roles = roles;
        this.permissions = permissions;
    }
    
    // Entity -> DTO 변환 (비밀번호 제외)
    public static UserDto fromEntity(User user) {
        Set<String> roles = user.getRoles() != null ? 
            user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet()) : null;
                
        Set<String> permissions = user.getRoles() != null ?
            user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .collect(Collectors.toSet()) : null;
        
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getEnabled(),
            user.getAccountNonExpired(),
            user.getAccountNonLocked(),
            user.getCredentialsNonExpired(),
            user.getCreatedAt(),
            user.getLastLoginAt(),
            roles,
            permissions
        );
    }
    
    // DTO -> Entity 변환
    public User toEntity() {
        User user = new User(this.username, this.password, this.email);
        if (this.enabled != null) user.setEnabled(this.enabled);
        if (this.accountNonExpired != null) user.setAccountNonExpired(this.accountNonExpired);
        if (this.accountNonLocked != null) user.setAccountNonLocked(this.accountNonLocked);
        if (this.credentialsNonExpired != null) user.setCredentialsNonExpired(this.credentialsNonExpired);
        return user;
    }
    
    // 권한 체크 메서드들
    public boolean hasRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }
    
    public boolean hasPermission(String permissionName) {
        return permissions != null && permissions.contains(permissionName);
    }
    
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }
}