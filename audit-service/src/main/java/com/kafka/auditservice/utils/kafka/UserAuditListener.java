package com.kafka.auditservice.utils.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.auditservice.domain.dtos.audit.UserAuditDto;
import com.kafka.auditservice.domain.dtos.commons.KafkaEventsDto;
import com.kafka.auditservice.services.contract.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @KafkaListener(
            topics = "#{'${kafka.topic.user-and-audit.name}'}",
            groupId = "${kafka.topic.user-register.group-id}",
            containerFactory = "userRegisterContainerFactory"
    )
    public void userAuditListener(KafkaEventsDto<UserAuditDto> kafkaEvents){
        try{
            LOGGER.info("Received user audit event: {}", kafkaEvents.getEventType());
            switch (kafkaEvents.getEventType()){
                case USER_REGISTERED:
                    UserAuditDto userAuditDto = convertTo(kafkaEvents.getData(), UserAuditDto.class);
                    userAuditDto.setSource_service("User Service");
                    auditService.createAudit(userAuditDto);
                    break;

                default:
                    LOGGER.warn("Unhandled user audit event type: {}", kafkaEvents.getEventType());
                    break;
            }
        }catch (Exception e) {
            LOGGER.error("Error processing user audit event: {}", e.getMessage());
        }
    }

    private <T> T convertTo(Object data, Class<T> type){
        return objectMapper.convertValue(data, type);
    }
}
