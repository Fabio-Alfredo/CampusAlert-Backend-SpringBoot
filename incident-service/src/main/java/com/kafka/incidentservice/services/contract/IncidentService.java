package com.kafka.incidentservice.services.contract;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.enums.IncidentStatus;
import com.kafka.incidentservice.domain.models.Incident;

import java.util.List;
import java.util.UUID;

public interface IncidentService {

    void createIncident(RegisterIncidentDto incidentDto, UserDto user);
    List<Incident>findAllIncidents();
    Incident assignSecurityInIncident(UUID security, UUID incidentId);
    Incident updateStatus(UUID incidentId, IncidentStatus status);

}
