package com.kafka.incidentservice.domain.dtos.incident;

import com.kafka.incidentservice.domain.dtos.common.KafkaEvents;
import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditIncidentDto {
    private KafkaEventTypes eventType;
    private String payload;
}
