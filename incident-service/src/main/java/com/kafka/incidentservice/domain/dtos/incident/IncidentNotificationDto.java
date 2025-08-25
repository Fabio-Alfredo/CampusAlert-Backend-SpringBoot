package com.kafka.incidentservice.domain.dtos.incident;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class IncidentNotificationDto {
    private String eventType;
    private String email;
    private String message;
    private Map<String, Object>metadata;
}
