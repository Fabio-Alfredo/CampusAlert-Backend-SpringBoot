package com.kafka.incidentservice.services.impl;

import com.kafka.incidentservice.domain.dtos.common.KafkaEvents;
import com.kafka.incidentservice.domain.dtos.incident.AuditIncidentDto;
import com.kafka.incidentservice.domain.dtos.incident.IncidentDto;
import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import com.kafka.incidentservice.domain.models.Incident;
import com.kafka.incidentservice.services.contract.IncidentAuditPublisher;
import com.kafka.incidentservice.uitls.common.MapperTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IncidentAuditPublisherImpl implements IncidentAuditPublisher {

    @Value("${kafka.topic.incident-and-audit.name}")
    private String incidentTopic;

    private final MapperTools mapperTools;
    private final KafkaTemplate<String, KafkaEvents<AuditIncidentDto>> kafkaTemplate;

    public IncidentAuditPublisherImpl(MapperTools mapperTools, KafkaTemplate<String, KafkaEvents<AuditIncidentDto>> kafkaTemplate) {
        this.mapperTools = mapperTools;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAuditEvent(Incident incident, KafkaEventTypes action, UUID editedBy) {
        try{
            AuditIncidentDto auditIncidentDto = new AuditIncidentDto();
            auditIncidentDto.setEventType(action);

            IncidentDto incidentDto = mapperTools.convertTo(incident, IncidentDto.class);
            incidentDto.setUpdatedBy(editedBy);
            auditIncidentDto.setPayload(mapperTools.convertToString(incidentDto));

            kafkaTemplate.send(incidentTopic, new KafkaEvents<>(action, auditIncidentDto));
        }catch (Exception e){
            throw new RuntimeException("Error publishing incident audit event: " + e.getMessage(), e);
        }
    }
}
