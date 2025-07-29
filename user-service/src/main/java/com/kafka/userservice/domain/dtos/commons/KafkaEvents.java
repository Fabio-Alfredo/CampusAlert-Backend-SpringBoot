package com.kafka.userservice.domain.dtos.commons;


import com.kafka.userservice.domain.enums.KafkaEventTypes;

public class KafkaEvents<T>{
    private KafkaEventTypes eventType;
    private T data;

    public KafkaEvents() {
    }

    public KafkaEvents(KafkaEventTypes eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public KafkaEventTypes getEventType() {
        return eventType;
    }

    public void setEventType(KafkaEventTypes eventType) {
        this.eventType = eventType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
