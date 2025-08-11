package com.kafka.auditservice.utils.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.auditservice.domain.dtos.audit.EventAuditDto;
import com.kafka.auditservice.domain.dtos.commons.KafkaEventsDto;
import com.kafka.auditservice.services.contract.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserAuditListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuditListener.class);
    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    public UserAuditListener(ObjectMapper objectMapper, AuditService auditService) {
        this.objectMapper = objectMapper;
        this.auditService = auditService;
    }


    private <T> T convertTo(Object data, Class<T> type){
        return objectMapper.convertValue(data, type);
    }

    @KafkaListener(
            topics = {"${kafka.topic.user-and-audit.name}", "${kafka.topic.incident-and-audit.name}"},
            groupId = "${kafka.topic.user-register.group-id}",
            containerFactory = "userRegisterContainerFactory"
    )
    public void userAuditListener(KafkaEventsDto<EventAuditDto> kafkaEvents){
        try{
            LOGGER.info("Received user audit event: {}", kafkaEvents.getEventType());
            switch (kafkaEvents.getEventType()){
                case USER_REGISTERED:
                    EventAuditDto userAuditDto = convertTo(kafkaEvents.getData(), EventAuditDto.class);
                    userAuditDto.setSource_service("User Service");
                    auditService.createAudit(userAuditDto);
                    break;
                case REGISTER_INCIDENT:
                    EventAuditDto incidentAuditDto = convertTo(kafkaEvents.getData(), EventAuditDto.class);
                    incidentAuditDto.setSource_service("Incident Register");
                    auditService.createAudit(incidentAuditDto);
                    break;
                case ASSIGN_SECURITY:
                    EventAuditDto assignSecurityAuditDto = convertTo(kafkaEvents.getData(), EventAuditDto.class);
                    assignSecurityAuditDto.setSource_service("Incident Assign Security");
                    auditService.createAudit(assignSecurityAuditDto);
                    break;
                default:
                    LOGGER.warn("Unhandled user audit event type: {}", kafkaEvents.getEventType());
                    break;
            }
        }catch (Exception e) {
            LOGGER.error("Error processing user audit event: {}", e.getMessage());
        }
    }
}
