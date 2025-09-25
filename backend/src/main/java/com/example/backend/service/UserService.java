package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.entity.Role;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // 모든 사용자 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::fromEntity);
    }
    
    // 사용자 상세 조회 (ID로)
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserDto.fromEntity(user);
    }
    
    // 사용자 상세 조회 (사용자명으로)
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserDto.fromEntity(user);
    }
    
    // 사용자 등록
    public UserDto createUser(UserDto userDto) {
        // 사용자명 중복 확인
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        
        // 이메일 중복 확인
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = userDto.toEntity();
        user.setPassword(encodedPassword);
        
        // 기본 역할 할당 (ROLE_USER)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("기본 역할을 찾을 수 없습니다."));
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }
    
    // 관리자 계정 생성
    public UserDto createAdmin(UserDto userDto) {
        // 사용자명 중복 확인
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        
        // 이메일 중복 확인
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = userDto.toEntity();
        user.setPassword(encodedPassword);
        
        // 관리자 역할 할당
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("관리자 역할을 찾을 수 없습니다."));
        
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }
    
    // 사용자 정보 수정
    public UserDto updateUser(Long id, UserDto userDto, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 본인 확인
        if (!user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("사용자 정보를 수정할 권한이 없습니다.");
        }
        
        // 사용자명 변경 시 중복 확인
        if (!user.getUsername().equals(userDto.getUsername()) && 
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        
        // 이메일 변경 시 중복 확인
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        // 정보 업데이트
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        
        // 비밀번호 변경 시
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encodedPassword);
        }
        
        User updatedUser = userRepository.save(user);
        return UserDto.fromEntity(updatedUser);
    }
    
    // 사용자 삭제
    public void deleteUser(Long id, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 본인 확인
        if (!user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("사용자를 삭제할 권한이 없습니다.");
        }
        
        userRepository.delete(user);
    }
    
    // ==================== RBAC 관련 메서드 ====================
    
    // 사용자에게 역할 할당
    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다: " + roleName));
        
        user.getRoles().add(role);
        userRepository.save(user);
    }
    
    // 사용자에게서 역할 제거
    public void removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("역할을 찾을 수 없습니다: " + roleName));
        
        user.getRoles().remove(role);
        userRepository.save(user);
    }
    
    // 사용자 계정 활성화/비활성화
    public void toggleUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        user.setEnabled(enabled);
        userRepository.save(user);
    }
    
    // 사용자 계정 잠금/해제
    public void toggleUserLock(Long userId, boolean locked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        user.setAccountNonLocked(!locked);
        userRepository.save(user);
    }
    
    // 사용자 역할 확인
    @Transactional(readOnly = true)
    public boolean hasRole(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return user.hasRole(roleName);
    }
    
    // 사용자 권한 확인
    @Transactional(readOnly = true)
    public boolean hasPermission(String username, String permissionName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return user.hasPermission(permissionName);
    }
    
    // ==================== 기존 메서드들 ====================
    
    // 사용자명으로 검색
    @Transactional(readOnly = true)
    public Page<UserDto> searchByUsername(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable)
                .map(UserDto::fromEntity);
    }
    
    // 이메일로 검색
    @Transactional(readOnly = true)
    public Page<UserDto> searchByEmail(String email, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCase(email, pageable)
                .map(UserDto::fromEntity);
    }
    
    // 사용자명 중복 체크
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    // 이메일 중복 체크
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // 로그인 검증
    @Transactional(readOnly = true)
    public UserDto authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다."));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다.");
        }
        
        if (!user.getEnabled()) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }
        
        if (!user.getAccountNonLocked()) {
            throw new RuntimeException("잠긴 계정입니다.");
        }
        
        return UserDto.fromEntity(user);
    }
    
    // 최근 가입한 사용자 조회
    @Transactional(readOnly = true)
    public List<UserDto> getRecentUsers(Pageable pageable) {
        return userRepository.findRecentUsers(pageable)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 게시글을 많이 작성한 사용자 조회
    @Transactional(readOnly = true)
    public List<UserDto> getTopUsersByArticleCount(Pageable pageable) {
        return userRepository.findTopUsersByArticleCount(pageable)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // ==================== 관리자 통계용 메서드 ====================
    
    @Transactional(readOnly = true)
    public Long getTotalUserCount() {
        return userRepository.count();
    }
    
    @Transactional(readOnly = true)
    public Long getActiveUserCount() {
        return userRepository.countByEnabled(true);
    }
    
    @Transactional(readOnly = true)
    public Long getNewUsersThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        return userRepository.countUsersByDateRange(startOfMonth, endOfMonth);
    }
}