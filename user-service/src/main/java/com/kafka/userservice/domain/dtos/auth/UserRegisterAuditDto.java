package com.kafka.userservice.domain.dtos.auth;


import com.kafka.userservice.domain.enums.KafkaEventTypes;

public class UserRegisterAuditDto {
    private KafkaEventTypes eventType;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public UserRegisterAuditDto() {

    }

    public UserRegisterAuditDto(KafkaEventTypes eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public KafkaEventTypes getEventType() {
        return eventType;
    }

    public void setEventType(KafkaEventTypes eventType) {
        this.eventType = eventType;
    }
}
