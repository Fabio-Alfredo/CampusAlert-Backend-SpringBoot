package com.kafka.incidentservice.domain.dtos.incident;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusDto {
    private UUID incidentId;
    private String status;
}
