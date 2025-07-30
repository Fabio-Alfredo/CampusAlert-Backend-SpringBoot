package com.kafka.auditservice.domain.dtos.commons;

import com.kafka.auditservice.domain.enums.EventType;
import lombok.Data;

@Data
public class KafkaEventsDto<T> {
    private String eventType;
    private T data;
}
