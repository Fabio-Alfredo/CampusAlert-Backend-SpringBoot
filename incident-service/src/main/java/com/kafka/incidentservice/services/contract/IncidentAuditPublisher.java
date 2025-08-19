package com.kafka.incidentservice.services.contract;

import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import com.kafka.incidentservice.domain.models.Incident;

import java.util.UUID;

public interface IncidentAuditPublisher {
    void publishAuditEvent(Incident incident, KafkaEventTypes action, UUID editedBy);
    void notifyEvents(Incident incident, KafkaEventTypes action, String editedBy);
}
