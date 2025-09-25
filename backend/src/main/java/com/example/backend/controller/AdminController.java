package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.Role;
import com.example.backend.entity.Permission;
import com.example.backend.service.UserService;
import com.example.backend.service.RoleService;
import com.example.backend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private PermissionService permissionService;
    
    // ==================== 사용자 관리 ====================
    
    // 모든 사용자 조회 (관리자만)
    @GetMapping("/users")
    @PreAuthorize("hasPermission('users', 'READ')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserDto> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    // 사용자에게 역할 할당
    @PostMapping("/users/{userId}/roles/{roleName}")
    @PreAuthorize("hasPermission('users', 'MANAGE')")
    public ResponseEntity<Map<String, Object>> assignRole(
            @PathVariable Long userId, 
            @PathVariable String roleName) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.assignRole(userId, roleName);
            response.put("success", true);
            response.put("message", "역할이 성공적으로 할당되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 사용자에게서 역할 제거
    @DeleteMapping("/users/{userId}/roles/{roleName}")
    @PreAuthorize("hasPermission('users', 'MANAGE')")
    public ResponseEntity<Map<String, Object>> removeRole(
            @PathVariable Long userId, 
            @PathVariable String roleName) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.removeRole(userId, roleName);
            response.put("success", true);
            response.put("message", "역할이 성공적으로 제거되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 사용자 계정 활성화/비활성화
    @PatchMapping("/users/{userId}/status")
    @PreAuthorize("hasPermission('users', 'MANAGE')")
    public ResponseEntity<Map<String, Object>> toggleUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.toggleUserStatus(userId, enabled);
            response.put("success", true);
            response.put("message", enabled ? "사용자가 활성화되었습니다." : "사용자가 비활성화되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // ==================== 역할 관리 ====================
    
    // 모든 역할 조회
    @GetMapping("/roles")
    @PreAuthorize("hasPermission('admin', 'READ')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    // 새 역할 생성
    @PostMapping("/roles")
    @PreAuthorize("hasPermission('admin', 'CREATE')")
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody Role role) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Role createdRole = roleService.createRole(role);
            response.put("success", true);
            response.put("message", "역할이 성공적으로 생성되었습니다.");
            response.put("data", createdRole);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 역할에 권한 할당
    @PostMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasPermission('admin', 'MANAGE')")
    public ResponseEntity<Map<String, Object>> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody Set<String> permissionNames) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            roleService.assignPermissions(roleId, permissionNames);
            response.put("success", true);
            response.put("message", "권한이 성공적으로 할당되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // ==================== 권한 관리 ====================
    
    // 모든 권한 조회
    @GetMapping("/permissions")
    @PreAuthorize("hasPermission('admin', 'READ')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
    
    // 새 권한 생성
    @PostMapping("/permissions")
    @PreAuthorize("hasPermission('admin', 'CREATE')")
    public ResponseEntity<Map<String, Object>> createPermission(@RequestBody Permission permission) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Permission createdPermission = permissionService.createPermission(permission);
            response.put("success", true);
            response.put("message", "권한이 성공적으로 생성되었습니다.");
            response.put("data", createdPermission);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // ==================== 시스템 통계 ====================
    
    // 관리자 대시보드 통계
    @GetMapping("/dashboard")
    @PreAuthorize("hasPermission('admin', 'READ')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 사용자 통계
        stats.put("totalUsers", userService.getTotalUserCount());
        stats.put("activeUsers", userService.getActiveUserCount());
        stats.put("newUsersThisMonth", userService.getNewUsersThisMonth());
        
        // 기타 통계들...
        
        return ResponseEntity.ok(stats);
    }
    
    // 시스템 로그 조회
    @GetMapping("/logs")
    @PreAuthorize("hasPermission('admin', 'READ')")
    public ResponseEntity<List<String>> getSystemLogs(
            @RequestParam(defaultValue = "100") int limit) {
        
        // 실제로는 로그 서비스에서 가져와야 함
        List<String> logs = List.of("System log entries...");
        return ResponseEntity.ok(logs);
    }
}