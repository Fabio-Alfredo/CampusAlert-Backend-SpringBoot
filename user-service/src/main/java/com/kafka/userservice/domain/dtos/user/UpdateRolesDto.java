package com.kafka.userservice.domain.dtos.user;


import java.util.UUID;

public class UpdateRolesDto {
    private UUID userId;
    private String roleId;
    private String action;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
