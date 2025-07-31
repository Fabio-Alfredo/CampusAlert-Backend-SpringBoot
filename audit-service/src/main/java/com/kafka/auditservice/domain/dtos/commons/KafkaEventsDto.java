package com.kafka.auditservice.domain.dtos.commons;

import com.kafka.auditservice.domain.enums.EventType;
import lombok.Data;

@Data
public class KafkaEventsDto<T> {
    private EventType eventType;
    private T data;
}
