package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 모든 사용자 조회 (페이징) - 관리자용
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDto> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    // 사용자 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 현재 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            UserDto user = userService.getUserByUsername(username);
            response.put("success", true);
            response.put("data", user);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // 사용자 등록 (회원가입)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserDto createdUser = userService.createUser(userDto);
            response.put("success", true);
            response.put("message", "사용자가 성공적으로 등록되었습니다.");
            response.put("data", createdUser);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserDto loginDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            UserDto user = userService.authenticate(loginDto.getUsername(), loginDto.getPassword());
            response.put("success", true);
            response.put("message", "로그인 성공");
            response.put("data", user);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    // 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            UserDto updatedUser = userService.updateUser(id, userDto, username);
            response.put("success", true);
            response.put("message", "사용자 정보가 성공적으로 수정되었습니다.");
            response.put("data", updatedUser);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 사용자 삭제 (회원탈퇴)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = getCurrentUsername();
            if (username == null) {
                response.put("success", false);
                response.put("message", "인증이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            userService.deleteUser(id, username);
            response.put("success", true);
            response.put("message", "사용자가 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 사용자 검색
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) String type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<UserDto> users;
        
        if (type == null || type.equals("username")) {
            users = userService.searchByUsername(keyword, pageable);
        } else if (type.equals("email")) {
            users = userService.searchByEmail(keyword, pageable);
        } else {
            users = userService.searchByUsername(keyword, pageable);
        }
        
        return ResponseEntity.ok(users);
    }
    
    // 사용자명 중복 체크
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameExists(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.isUsernameExists(username);
        
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 사용자명입니다." : "사용 가능한 사용자명입니다.");
        
        return ResponseEntity.ok(response);
    }
    
    // 이메일 중복 체크
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = userService.isEmailExists(email);
        
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        
        return ResponseEntity.ok(response);
    }
    
    // 최근 가입한 사용자 조회
    @GetMapping("/recent")
    public ResponseEntity<List<UserDto>> getRecentUsers(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<UserDto> recentUsers = userService.getRecentUsers(pageable);
        
        return ResponseEntity.ok(recentUsers);
    }
    
    // 게시글을 많이 작성한 사용자 조회
    @GetMapping("/top/articles")
    public ResponseEntity<List<UserDto>> getTopUsersByArticleCount(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<UserDto> topUsers = userService.getTopUsersByArticleCount(pageable);
        
        return ResponseEntity.ok(topUsers);
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