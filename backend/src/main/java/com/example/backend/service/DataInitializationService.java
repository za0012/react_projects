package com.example.backend.service;

import com.example.backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements ApplicationRunner {
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeRBAC();
        createDefaultAdmin();
    }
    
    private void initializeRBAC() {
        System.out.println("RBAC 초기화 중...");
        
        try {
            // 1. 기본 권한 생성
            permissionService.initializeDefaultPermissions();
            System.out.println("✅ 기본 권한들이 생성되었습니다.");
            
            // 2. 기본 역할 생성
            roleService.initializeDefaultRoles();
            System.out.println("✅ 기본 역할들이 생성되었습니다.");
            
            // 3. 역할에 권한 할당
            permissionService.assignDefaultPermissions();
            System.out.println("✅ 역할에 권한이 할당되었습니다.");
            
        } catch (Exception e) {
            System.err.println("❌ RBAC 초기화 실패: " + e.getMessage());
        }
    }
    
    private void createDefaultAdmin() {
        System.out.println("기본 관리자 계정 생성 중...");
        
        try {
            // 기본 관리자 계정이 없으면 생성
            if (!userService.isUsernameExists("admin")) {
                UserDto adminDto = new UserDto();
                adminDto.setUsername("admin");
                adminDto.setPassword("admin123!@#");
                adminDto.setEmail("admin@cookierun.com");
                
                UserDto createdAdmin = userService.createAdmin(adminDto);
                System.out.println("✅ 기본 관리자 계정이 생성되었습니다.");
                System.out.println("   사용자명: admin");
                System.out.println("   비밀번호: admin123!@#");
                System.out.println("   이메일: admin@cookierun.com");
            } else {
                System.out.println("ℹ️  관리자 계정이 이미 존재합니다.");
            }
            
            // 테스트용 매니저 계정 생성
            if (!userService.isUsernameExists("manager")) {
                UserDto managerDto = new UserDto();
                managerDto.setUsername("manager");
                managerDto.setPassword("manager123");
                managerDto.setEmail("manager@cookierun.com");
                
                UserDto createdManager = userService.createUser(managerDto);
                userService.assignRole(createdManager.getId(), "ROLE_MANAGER");
                System.out.println("✅ 매니저 계정이 생성되었습니다.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ 기본 계정 생성 실패: " + e.getMessage());
        }
    }
}