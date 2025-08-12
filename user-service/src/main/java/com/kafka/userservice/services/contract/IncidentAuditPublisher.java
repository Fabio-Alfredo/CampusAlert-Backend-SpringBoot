package com.kafka.userservice.services.contract;

import com.kafka.userservice.domain.enums.KafkaEventTypes;
import com.kafka.userservice.domain.models.User;

import java.util.UUID;

public interface IncidentAuditPublisher {
    void publishAuditEvent(User user, KafkaEventTypes action, UUID editedBy);
}
