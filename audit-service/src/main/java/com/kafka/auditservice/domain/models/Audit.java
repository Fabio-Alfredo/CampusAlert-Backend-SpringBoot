package com.kafka.auditservice.domain.models;

import com.kafka.auditservice.domain.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "event_type", nullable = false)
    private EventType eventType;
    @Column(name = "source_service", nullable = false)
    private String sourceService;
    @Column(name = "payload", nullable = false)
    private String payload;

//    private LocalDateTime timestamp;
//
//    @PrePersist
//    public void prePersist() {
//        if (timestamp == null) {
//            timestamp = LocalDateTime.now();
//        }
//    }

}
