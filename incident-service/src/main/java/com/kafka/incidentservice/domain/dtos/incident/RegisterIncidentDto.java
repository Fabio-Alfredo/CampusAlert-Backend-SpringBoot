package com.kafka.incidentservice.domain.dtos.incident;

import com.kafka.incidentservice.domain.enums.IncidentType;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterIncidentDto {
    @NotEmpty(message = "Title cannot be empty")
    private String title;
    private String description;
    private IncidentType incidentType;
}
