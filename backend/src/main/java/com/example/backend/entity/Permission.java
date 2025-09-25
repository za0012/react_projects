package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name; // READ_COOKIES, WRITE_COOKIES, DELETE_COOKIES, MANAGE_USERS 등
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private String resource; // cookies, pets, users, admin
    
    @Column(nullable = false)
    private String action; // CREATE, READ, UPDATE, DELETE, MANAGE
    
    private LocalDateTime createdAt;
    
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
    
    // 기본 생성자
    public Permission() {
        this.createdAt = LocalDateTime.now();
    }
    
    // 생성자
    public Permission(String name, String description, String resource, String action) {
        this();
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
    }
    
    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}