package com.kafka.auditservice.domain.enums;

public enum EventType {
    REGISTER_INCIDENT,
    USER_REGISTERED;

    public static EventType fromString(String eventType){
        for (EventType type: EventType.values()) {
            if (type.name().equalsIgnoreCase(eventType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + eventType);
    }
}
