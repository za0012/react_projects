package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    // 현재 사용자의 권한 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            UserDto user = userService.getUserByUsername(username);
            
            // 권한 정보 추가
            Map<String, Object> profile = new HashMap<>();
            profile.put("user", user);
            profile.put("roles", user.getRoles());
            profile.put("permissions", user.getPermissions());
            profile.put("isAdmin", user.isAdmin());
            profile.put("isManager", user.isManager());
            profile.put("highestRole", determineHighestRole(user));
            
            // 기능별 권한 체크
            Map<String, Boolean> capabilities = new HashMap<>();
            capabilities.put("canManageCookies", user.hasPermission("MANAGE_COOKIES") || user.hasPermission("WRITE_COOKIES"));
            capabilities.put("canManagePets", user.hasPermission("MANAGE_PETS") || user.hasPermission("WRITE_PETS"));
            capabilities.put("canManageUsers", user.hasPermission("MANAGE_USERS"));
            capabilities.put("canAccessAdmin", user.hasPermission("ADMIN_ACCESS"));
            capabilities.put("canDeleteArticles", user.hasPermission("DELETE_ARTICLES") || user.isAdmin());
            capabilities.put("canDeleteComments", user.hasPermission("DELETE_COMMENTS") || user.isAdmin());
            
            profile.put("capabilities", capabilities);
            
            response.put("success", true);
            response.put("data", profile);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // 특정 권한 체크
    @GetMapping("/check-permission/{permission}")
    public ResponseEntity<Map<String, Object>> checkPermission(@PathVariable String permission) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("hasPermission", false);
                response.put("authenticated", false);
                return ResponseEntity.ok(response);
            }
            
            boolean hasPermission = userService.hasPermission(username, permission);
            
            response.put("hasPermission", hasPermission);
            response.put("authenticated", true);
            response.put("permission", permission);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("hasPermission", false);
            response.put("authenticated", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 특정 역할 체크
    @GetMapping("/check-role/{role}")
    public ResponseEntity<Map<String, Object>> checkRole(@PathVariable String role) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("hasRole", false);
                response.put("authenticated", false);
                return ResponseEntity.ok(response);
            }
            
            boolean hasRole = userService.hasRole(username, role);
            
            response.put("hasRole", hasRole);
            response.put("authenticated", true);
            response.put("role", role);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("hasRole", false);
            response.put("authenticated", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 현재 인증 상태 확인
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Map<String, Object> response = new HashMap<>();
        
        String username = getCurrentUsername();
        boolean isAuthenticated = username != null;
        
        response.put("authenticated", isAuthenticated);
        
        if (isAuthenticated) {
            try {
                UserDto user = userService.getUserByUsername(username);
                response.put("username", username);
                response.put("roles", user.getRoles());
                response.put("isAdmin", user.isAdmin());
                response.put("isManager", user.isManager());
                response.put("highestRole", determineHighestRole(user));
            } catch (Exception e) {
                response.put("error", "사용자 정보 조회 실패");
            }
        }
        
        return ResponseEntity.ok(response);
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
    
    // 현재 사용자명 가져오기
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName();
        }
        return null;
    }
}