package com.kafka.incidentservice.domain.dtos.incident;

import lombok.Data;

import java.util.UUID;

@Data
public class IncidentNotificationDto {
    private UUID incidentId;
    private String Email;
}
