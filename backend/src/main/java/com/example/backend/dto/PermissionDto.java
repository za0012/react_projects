package com.example.backend.dto;

import com.example.backend.entity.Permission;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class PermissionDto {
    private Long id;
    
    @NotBlank(message = "권한 이름은 필수입니다.")
    private String name;
    
    private String description;
    
    @NotBlank(message = "리소스는 필수입니다.")
    private String resource;
    
    @NotBlank(message = "액션은 필수입니다.")
    private String action;
    
    private LocalDateTime createdAt;
    private List<String> roleNames; // 이 권한을 가진 역할들의 이름
    
    // 전체 생성자
    public PermissionDto(Long id, String name, String description, String resource, 
                        String action, LocalDateTime createdAt, List<String> roleNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
        this.createdAt = createdAt;
        this.roleNames = roleNames;
    }
    
    // Entity -> DTO 변환
    public static PermissionDto fromEntity(Permission permission) {
        List<String> roleNames = null;
        if (permission.getRoles() != null) {
            roleNames = permission.getRoles().stream()
                    .map(role -> role.getName())
                    .toList();
        }
        
        return new PermissionDto(
            permission.getId(),
            permission.getName(),
            permission.getDescription(),
            permission.getResource(),
            permission.getAction(),
            permission.getCreatedAt(),
            roleNames
        );
    }
    
    // DTO -> Entity 변환
    public Permission toEntity() {
        Permission permission = new Permission();
        permission.setName(this.name);
        permission.setDescription(this.description);
        permission.setResource(this.resource);
        permission.setAction(this.action);
        return permission;
    }
}