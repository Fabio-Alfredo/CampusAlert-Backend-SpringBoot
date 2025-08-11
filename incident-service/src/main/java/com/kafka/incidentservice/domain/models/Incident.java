package com.kafka.incidentservice.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kafka.incidentservice.domain.enums.IncidentStatus;
import com.kafka.incidentservice.domain.enums.IncidentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private IncidentType incidentType;
    private IncidentStatus incidentStatus;
    private UUID reportedBy;
    private UUID assignedTo;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime reportedAt;

    @PrePersist
    private void prePersist(){
//        if (reportedAt == null) {
//            reportedAt = LocalDateTime.now();
//        }
        if (incidentStatus == null) {
            incidentStatus = IncidentStatus.REPORTED;
        }
        if(incidentType == null) {
            incidentType = IncidentType.OTHER;
        }
    }



}
