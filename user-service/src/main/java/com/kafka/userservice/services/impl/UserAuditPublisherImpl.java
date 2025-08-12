package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.dtos.auth.UserRegisterAuditDto;
import com.kafka.userservice.domain.dtos.commons.KafkaEvents;
import com.kafka.userservice.domain.dtos.user.UserDto;
import com.kafka.userservice.domain.enums.KafkaEventTypes;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.services.contract.UserAuditPublisher;
import com.kafka.userservice.utils.common.MapperTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserAuditPublisherImpl implements UserAuditPublisher {

    @Value("${kafka.topic.user-and-audit.name}")
    private String userAndAuditTopic;

    private final MapperTools mapperTools;
    private final KafkaTemplate<String, KafkaEvents<UserRegisterAuditDto>> kafkaTemplate;

    public UserAuditPublisherImpl(MapperTools mapperTools, KafkaTemplate<String, KafkaEvents<UserRegisterAuditDto>> kafkaTemplate) {
        this.mapperTools = mapperTools;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAuditEvent(User user, KafkaEventTypes action, UUID editedBy) {
        try{
            UserRegisterAuditDto auditDto = new UserRegisterAuditDto();

            UserDto userDto = mapperTools.convertTo(user, UserDto.class);
            userDto.setUpdatingBy(editedBy);
            auditDto.setPayload(mapperTools.convertToString(userDto));
            auditDto.setEventType(action);

            kafkaTemplate.send(userAndAuditTopic, new KafkaEvents<>(action, auditDto));

        }catch (Exception e){
            throw new RuntimeException("Error publishing audit event: " + e.getMessage(), e);
        }
    }
}
