package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 전용으로만 사용
@Builder
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER
    
    @Column(length = 500)
    private String description;
    
    @Builder.Default // 이렇게 하면 기본 생성자와 Builder 모두에서 기본값 설정됨
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
    
    // 일반적인 생성자 (필요한 경우)
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}