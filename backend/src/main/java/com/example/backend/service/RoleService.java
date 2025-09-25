package com.example.backend.service;

// import com.example.backend.dto.RoleDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.Permission;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
// import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    // 모든 역할 조회
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    // 역할 이름으로 조회
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다: " + name));
    }
    
    // 새 역할 생성
    public Role createRole(Role role) {
        // 역할 이름 중복 체크
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("이미 존재하는 역할입니다: " + role.getName());
        }
        
        return roleRepository.save(role);
    }
    
    // 역할 수정
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다."));
        
        // 이름 변경 시 중복 체크
        if (!role.getName().equals(roleDetails.getName()) && 
            roleRepository.existsByName(roleDetails.getName())) {
            throw new RuntimeException("이미 존재하는 역할 이름입니다: " + roleDetails.getName());
        }
        
        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        
        return roleRepository.save(role);
    }
    
    // 역할 삭제
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다."));
        
        // 기본 역할 삭제 방지
        if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_USER")) {
            throw new RuntimeException("기본 역할은 삭제할 수 없습니다.");
        }
        
        roleRepository.delete(role);
    }
    
    // 역할에 권한 할당
    public void assignPermissions(Long roleId, Set<String> permissionNames) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다."));
        
        Set<Permission> permissions = permissionRepository.findByNameIn(permissionNames);
        
        if (permissions.size() != permissionNames.size()) {
            throw new RuntimeException("일부 권한을 찾을 수 없습니다.");
        }
        
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
    
    // 역할에서 권한 제거
    public void removePermissions(Long roleId, Set<String> permissionNames) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다."));
        
        Set<Permission> currentPermissions = role.getPermissions();
        Set<Permission> permissionsToRemove = permissionRepository.findByNameIn(permissionNames);
        
        currentPermissions.removeAll(permissionsToRemove);
        roleRepository.save(role);
    }
    
    // 기본 역할들 초기화
    @Transactional
    public void initializeDefaultRoles() {
        // ROLE_ADMIN
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role("ROLE_ADMIN", "시스템 관리자 역할");
            roleRepository.save(adminRole);
        }
        
        // ROLE_MANAGER
        if (!roleRepository.existsByName("ROLE_MANAGER")) {
            Role managerRole = new Role("ROLE_MANAGER", "콘텐츠 관리자 역할");
            roleRepository.save(managerRole);
        }
        
        // ROLE_USER
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role("ROLE_USER", "일반 사용자 역할");
            roleRepository.save(userRole);
        }
    }
    
    // 기본 역할 조회
    @Transactional(readOnly = true)
    public Set<Role> getDefaultRoles() {
        return roleRepository.findDefaultRoles();
    }
}