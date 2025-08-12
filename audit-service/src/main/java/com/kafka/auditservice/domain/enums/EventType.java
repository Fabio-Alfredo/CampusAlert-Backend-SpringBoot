package com.kafka.auditservice.domain.enums;

public enum EventType {
    REGISTER_INCIDENT,
    USER_REGISTERED,
    ASSIGN_SECURITY,
    UPDATE_STATUS_INCIDENT,
    USER_UPDATING_PASSWORD,
    USER_UPDATING_ROLES;

    public static EventType fromString(String eventType){
        for (EventType type: EventType.values()) {
            if (type.name().equalsIgnoreCase(eventType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + eventType);
    }
}
