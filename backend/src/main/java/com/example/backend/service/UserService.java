package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
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
}