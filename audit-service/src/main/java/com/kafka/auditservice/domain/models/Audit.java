package com.kafka.auditservice.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kafka.auditservice.domain.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
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


//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime timestamp;
//
//    @PrePersist
//    public void prePersist() {
//        this.timestamp = LocalDateTime.now();
//    }

}
