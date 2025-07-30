package com.kafka.auditservice.configurations.kafka;

import com.kafka.auditservice.domain.dtos.commons.KafkaEventsDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumersConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object>consumersConfig(){
        Map<String, Object>properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Configuración del deserializador JSON
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaEventsDto.class.getName());

        return properties;
    }

    @Bean
    ConsumerFactory<String, KafkaEventsDto>userRegisterFactory(){
        return  new DefaultKafkaConsumerFactory<>(
                consumersConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(KafkaEventsDto.class)

        );
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, KafkaEventsDto>userRegisterContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, KafkaEventsDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userRegisterFactory());
        return factory;
    }
}
