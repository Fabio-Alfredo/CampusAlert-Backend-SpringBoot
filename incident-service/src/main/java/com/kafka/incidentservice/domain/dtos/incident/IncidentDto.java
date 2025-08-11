package com.kafka.incidentservice.domain.dtos.incident;

import com.kafka.incidentservice.domain.enums.IncidentStatus;
import com.kafka.incidentservice.domain.enums.IncidentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDto {
    private UUID id;
    private String title;
    private String description;
    private IncidentType incidentType;
    private IncidentStatus incidentStatus;
    private UUID reportedBy;
    private UUID assignedTo;
}
