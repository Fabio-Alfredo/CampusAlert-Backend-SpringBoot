package com.kafka.auditservice.utils.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.auditservice.domain.dtos.audit.EventAuditDto;
import com.kafka.auditservice.domain.dtos.commons.KafkaEventsDto;
import com.kafka.auditservice.services.contract.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    public void userAuditListener(KafkaEventsDto<EventAuditDto> kafkaEvents, Acknowledgment ack){
        try{
            LOGGER.info("Received user audit event: {}", kafkaEvents.getEventType());
            EventAuditDto eventAuditDto = convertTo(kafkaEvents.getData(), EventAuditDto.class);
            switch (kafkaEvents.getEventType()){
                case USER_REGISTERED:
                    eventAuditDto.setSource_service("User Service");
                    break;
                case REGISTER_INCIDENT:
                    eventAuditDto.setSource_service("Incident Register");
                    break;
                case ASSIGN_SECURITY:
                    eventAuditDto.setSource_service("Incident Assign Security");
                    break;
                case UPDATE_STATUS_INCIDENT:
                    eventAuditDto.setSource_service("Incident updating");
                    break;
                case USER_UPDATING_PASSWORD:
                    eventAuditDto.setSource_service("User updating");
                    break;
                case USER_UPDATING_ROLES:
                    eventAuditDto.setSource_service("Updated user roles");
                    break;
                default:
                    LOGGER.warn("Unhandled user audit event type: {}", kafkaEvents.getEventType());
                    return;
            }
            auditService.createAudit(eventAuditDto);
            ack.acknowledge();
        }catch (Exception e) {
            LOGGER.error("Error processing user audit event: {}", e.getMessage());
        }
    }
}
