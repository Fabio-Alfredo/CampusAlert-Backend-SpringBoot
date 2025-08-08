package com.kafka.incidentservice.domain.dtos.common;

import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaEvents<T> {
    private KafkaEventTypes eventType;
    private T data;
}
