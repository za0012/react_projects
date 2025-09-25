package com.example.backend.repository;

import com.example.backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // 권한 이름으로 조회
    Optional<Permission> findByName(String name);

    // 권한 이름이 존재하는지 확인
    boolean existsByName(String name);
    
    // 리소스별 권한 조회
    List<Permission> findByResource(String resource);

    // 특정 역할의 모든 권한 조회 (JOIN 쿼리 사용)
    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.name = :roleName")
    Set<Permission> findByRoleName(@Param("roleName") String roleName);
    
    // 여러 권한 이름으로 한 번에 조회
    Set<Permission> findByNameIn(Set<String> names);
}