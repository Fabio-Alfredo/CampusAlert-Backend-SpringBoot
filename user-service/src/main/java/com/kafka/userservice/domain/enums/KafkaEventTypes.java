package com.kafka.userservice.domain.enums;


public enum KafkaEventTypes {
    USER_REGISTERED,
    USER_UPDATING_PASSWORD,
    USER_UPDATING_ROLES;

    public static KafkaEventTypes fromString(String type){
        for (KafkaEventTypes event: KafkaEventTypes.values()){
            if(event.name().equalsIgnoreCase(type)){
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }
}
