package com.kafka.auditservice.utils.kafka;


import com.kafka.auditservice.domain.dtos.commons.KafkaEventsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserAuditListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuditListener.class);

    @KafkaListener(
            topics = "#{'${kafka.topic.user-and-audit.name}'}",
            groupId = "user-audit-service-group",
            containerFactory = "userRegisterContainerFactory"
    )
    public void userAuditListener(KafkaEventsDto<?> kafkaEvents){
        try{
            LOGGER.info("Received user audit event: {}", kafkaEvents.getEventType());
            switch (kafkaEvents.getEventType()){
                case "USER_REGISTERED":
                    System.out.println("User registered event received: " + kafkaEvents.getData());
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
