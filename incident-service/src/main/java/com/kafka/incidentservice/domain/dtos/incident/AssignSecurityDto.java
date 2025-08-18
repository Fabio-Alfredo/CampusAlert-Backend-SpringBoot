package com.kafka.incidentservice.domain.dtos.incident;

import lombok.Data;

import java.util.UUID;

@Data
public class AssignSecurityDto {
    private UUID securityId;
    private UUID incidentId;
}
