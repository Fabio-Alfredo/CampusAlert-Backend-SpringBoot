package com.kafka.auditservice.domain.dtos.audit;

import com.kafka.auditservice.domain.enums.EventType;
import lombok.Data;

@Data
public class EventAuditDto {
    private EventType eventType;
    private String payload;
    private String source_service;
}
