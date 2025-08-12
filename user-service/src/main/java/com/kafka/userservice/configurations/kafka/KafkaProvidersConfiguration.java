package com.kafka.userservice.configurations.kafka;

import com.kafka.userservice.domain.dtos.auth.UserRegisterAuditDto;
import com.kafka.userservice.domain.dtos.commons.KafkaEvents;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProvidersConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig(){
        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        //espera confirmación de todos los brokers antes de considerar el mensaje como enviado
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        //numero de reintentos en caso de fallo
        properties.put(ProducerConfig.RETRIES_CONFIG, 5);
        //tiempo de espera para reintentar el envío del mensaje (en milisegundos)
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        //tiempo de espera para que el productor considere que un mensaje ha fallado (en milisegundos)
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        //habilita la idempotencia del productor, lo que significa que los mensajes duplicados no se enviarán
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return  properties;
    }

    @Bean
    ProducerFactory<String, KafkaEvents<UserRegisterAuditDto>> userProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    KafkaTemplate<String, KafkaEvents<UserRegisterAuditDto>>userKafkaTemplate(ProducerFactory<String, KafkaEvents<UserRegisterAuditDto>>producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}
