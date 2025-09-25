package com.example.backend.service;

import com.example.backend.entity.Permission;
import com.example.backend.entity.Role;
import com.example.backend.repository.PermissionRepository;
import com.example.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PermissionService {
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    // 모든 권한 조회
    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
    
    // 권한 이름으로 조회
    @Transactional(readOnly = true)
    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다: " + name));
    }
    
    // 리소스별 권한 조회
    @Transactional(readOnly = true)
    public List<Permission> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource);
    }
    
    // 새 권한 생성
    public Permission createPermission(Permission permission) {
        // 권한 이름 중복 체크
        if (permissionRepository.existsByName(permission.getName())) {
            throw new RuntimeException("이미 존재하는 권한입니다: " + permission.getName());
        }
        
        return permissionRepository.save(permission);
    }
    
    // 권한 수정
    public Permission updatePermission(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));
        
        // 이름 변경 시 중복 체크
        if (!permission.getName().equals(permissionDetails.getName()) && 
            permissionRepository.existsByName(permissionDetails.getName())) {
            throw new RuntimeException("이미 존재하는 권한 이름입니다: " + permissionDetails.getName());
        }
        
        permission.setName(permissionDetails.getName());
        permission.setDescription(permissionDetails.getDescription());
        permission.setResource(permissionDetails.getResource());
        permission.setAction(permissionDetails.getAction());
        
        return permissionRepository.save(permission);
    }
    
    // 권한 삭제
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));
        
        permissionRepository.delete(permission);
    }
    
    // 특정 역할의 모든 권한 조회
    @Transactional(readOnly = true)
    public Set<Permission> getPermissionsByRole(String roleName) {
        return permissionRepository.findByRoleName(roleName);
    }
    
    // 기본 권한들 초기화
    @Transactional
    public void initializeDefaultPermissions() {
        // 쿠키 관련 권한
        createPermissionIfNotExists("READ_COOKIES", "쿠키 조회", "cookies", "READ");
        createPermissionIfNotExists("WRITE_COOKIES", "쿠키 생성/수정", "cookies", "WRITE");
        createPermissionIfNotExists("DELETE_COOKIES", "쿠키 삭제", "cookies", "DELETE");
        createPermissionIfNotExists("MANAGE_COOKIES", "쿠키 관리", "cookies", "MANAGE");
        
        // 펫 관련 권한
        createPermissionIfNotExists("READ_PETS", "펫 조회", "pets", "READ");
        createPermissionIfNotExists("WRITE_PETS", "펫 생성/수정", "pets", "WRITE");
        createPermissionIfNotExists("DELETE_PETS", "펫 삭제", "pets", "DELETE");
        createPermissionIfNotExists("MANAGE_PETS", "펫 관리", "pets", "MANAGE");
        
        // 사용자 관련 권한
        createPermissionIfNotExists("READ_USERS", "사용자 조회", "users", "READ");
        createPermissionIfNotExists("WRITE_USERS", "사용자 생성/수정", "users", "WRITE");
        createPermissionIfNotExists("DELETE_USERS", "사용자 삭제", "users", "DELETE");
        createPermissionIfNotExists("MANAGE_USERS", "사용자 관리", "users", "MANAGE");
        
        // 게시글 관련 권한
        createPermissionIfNotExists("READ_ARTICLES", "게시글 조회", "articles", "READ");
        createPermissionIfNotExists("WRITE_ARTICLES", "게시글 생성/수정", "articles", "WRITE");
        createPermissionIfNotExists("DELETE_ARTICLES", "게시글 삭제", "articles", "DELETE");
        
        // 댓글 관련 권한
        createPermissionIfNotExists("READ_COMMENTS", "댓글 조회", "comments", "READ");
        createPermissionIfNotExists("WRITE_COMMENTS", "댓글 생성/수정", "comments", "WRITE");
        createPermissionIfNotExists("DELETE_COMMENTS", "댓글 삭제", "comments", "DELETE");
        
        // 관리자 권한
        createPermissionIfNotExists("ADMIN_ACCESS", "관리자 접근", "admin", "READ");
        createPermissionIfNotExists("ADMIN_MANAGE", "관리자 관리", "admin", "MANAGE");
        createPermissionIfNotExists("SYSTEM_CONFIG", "시스템 설정", "admin", "CONFIG");
    }
    
    private void createPermissionIfNotExists(String name, String description, String resource, String action) {
        if (!permissionRepository.existsByName(name)) {
            Permission permission = new Permission(name, description, resource, action);
            permissionRepository.save(permission);
        }
    }
    
    // 역할에 기본 권한 할당
    @Transactional
    public void assignDefaultPermissions() {
        // ROLE_ADMIN - 모든 권한
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        if (adminRole != null) {
            Set<Permission> allPermissions = Set.copyOf(permissionRepository.findAll());
            adminRole.setPermissions(allPermissions);
            roleRepository.save(adminRole);
        }
        
        // ROLE_MANAGER - 콘텐츠 관리 권한
        Role managerRole = roleRepository.findByName("ROLE_MANAGER").orElse(null);
        if (managerRole != null) {
            Set<String> managerPermissions = Set.of(
                "READ_COOKIES", "WRITE_COOKIES", "DELETE_COOKIES", "MANAGE_COOKIES",
                "READ_PETS", "WRITE_PETS", "DELETE_PETS", "MANAGE_PETS",
                "READ_USERS", "READ_ARTICLES", "DELETE_ARTICLES",
                "READ_COMMENTS", "DELETE_COMMENTS"
            );
            Set<Permission> permissions = permissionRepository.findByNameIn(managerPermissions);
            managerRole.setPermissions(permissions);
            roleRepository.save(managerRole);
        }
        
        // ROLE_USER - 기본 사용자 권한
        Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
        if (userRole != null) {
            Set<String> userPermissions = Set.of(
                "READ_COOKIES", "READ_PETS",
                "READ_ARTICLES", "WRITE_ARTICLES",
                "READ_COMMENTS", "WRITE_COMMENTS"
            );
            Set<Permission> permissions = permissionRepository.findByNameIn(userPermissions);
            userRole.setPermissions(permissions);
            roleRepository.save(userRole);
        }
    }
}