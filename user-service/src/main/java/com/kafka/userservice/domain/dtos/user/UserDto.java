package com.kafka.userservice.domain.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private UUID id;
    private String userName;
    private String email;
    private UUID updatingBy;

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

    public UUID getUpdatingBy() {
        return updatingBy;
    }

    public void setUpdatingBy(UUID updatingBy) {
        this.updatingBy = updatingBy;
    }
}
