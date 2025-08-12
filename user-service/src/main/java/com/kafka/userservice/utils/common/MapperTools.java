package com.kafka.userservice.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperTools {

    private final ObjectMapper objectMapper;

    public MapperTools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T convertTo(Object data, Class<T> type){
        return objectMapper.convertValue(data, type);
    }

    public String convertToString(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
