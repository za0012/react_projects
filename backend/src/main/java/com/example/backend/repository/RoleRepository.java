package com.example.backend.repository;

import com.example.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // 역할 이름으로 찾기
    Optional<Role> findByName(String name);
    
    // 역할 이름 존재 여부 확인
    boolean existsByName(String name);
    
    // 기본 역할들 조회
    @Query("SELECT r FROM Role r WHERE r.name IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    Set<Role> findDefaultRoles();
    
    // 특정 권한을 가진 역할들 조회
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.name = :permissionName")
    Set<Role> findByPermissionName(String permissionName);
}