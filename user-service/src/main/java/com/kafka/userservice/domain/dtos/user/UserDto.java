package com.kafka.userservice.domain.dtos.user;

import com.kafka.userservice.domain.enums.AuthProvider;
import com.kafka.userservice.domain.models.User;

import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String userName;
    private String email;
    private String photo;
    private List<String> roles;
    private AuthProvider authProvider;

    public UserDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.photo = user.getPhoto();
        this.roles = user.getRoles().stream().map(role -> role.getName()).toList();
        this.authProvider = user.getAuthProvider();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
