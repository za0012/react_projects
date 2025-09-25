package com.example.backend.dto;

import com.example.backend.entity.Role;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class RoleDto {
    private Long id;
    
    @NotBlank(message = "역할 이름은 필수입니다.")
    private String name;
    
    private String description;
    private LocalDateTime createdAt;
    private List<String> userNames; // 이 역할을 가진 사용자들의 이름
    private List<String> permissionNames; // 이 역할의 권한들의 이름
    
    // 전체 생성자
    public RoleDto(Long id, String name, String description, LocalDateTime createdAt,
                   List<String> userNames, List<String> permissionNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.userNames = userNames;
        this.permissionNames = permissionNames;
    }
    
    // Entity -> DTO 변환
    public static RoleDto fromEntity(Role role) {
        List<String> userNames = null;
        if (role.getUsers() != null) {
            userNames = role.getUsers().stream()
                    .map(user -> user.getUsername())
                    .toList();
        }
        
        List<String> permissionNames = null;
        if (role.getPermissions() != null) {
            permissionNames = role.getPermissions().stream()
                    .map(permission -> permission.getName())
                    .toList();
        }
        
        return new RoleDto(
            role.getId(),
            role.getName(),
            role.getDescription(),
            role.getCreatedAt(),
            userNames,
            permissionNames
        );
    }
    
    // DTO -> Entity 변환
    public Role toEntity() {
        Role role = new Role();
        role.setName(this.name);
        role.setDescription(this.description);
        return role;
    }
}








