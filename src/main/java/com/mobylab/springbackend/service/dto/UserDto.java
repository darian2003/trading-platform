package com.mobylab.springbackend.service.dto;

import com.mobylab.springbackend.entity.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;

    public UUID getId() {
        return id;
    }

    public UserDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserDto setRoles(List<Role> roles) {
        this.roles = roles.stream()
                .map(Role::getName)
                .toList();
        return this;
    }
}