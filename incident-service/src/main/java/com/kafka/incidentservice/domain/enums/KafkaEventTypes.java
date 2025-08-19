package com.kafka.incidentservice.domain.enums;

public enum KafkaEventTypes {
    REGISTER_INCIDENT,
    UPDATE_STATUS_INCIDENT,
    ASSIGN_SECURITY,
    NOTIFY_NEW_INCIDENT;


    public static KafkaEventTypes fromString(String type){
        for (KafkaEventTypes event: KafkaEventTypes.values()){
            if(event.name().equalsIgnoreCase(type)){
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }
}
