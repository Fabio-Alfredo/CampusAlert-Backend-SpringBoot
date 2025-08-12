package com.kafka.userservice.configurations.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappersConfiguration {

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
