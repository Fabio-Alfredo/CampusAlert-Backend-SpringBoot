package com.kafka.userservice.domain.enums;

public enum RolesActions {
    ADD_ROLE,
    DELETE_ROLE;

    public static RolesActions fromString(String action) {
        for (RolesActions roleAction : RolesActions.values()) {
            if (roleAction.name().equalsIgnoreCase(action)) {
                return roleAction;
            }
        }
        throw new IllegalArgumentException("Unknown role action: " + action);
    }
}
