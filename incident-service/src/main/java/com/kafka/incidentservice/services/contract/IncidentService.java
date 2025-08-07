package com.kafka.incidentservice.services.contract;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.models.Incident;

import java.util.List;
import java.util.UUID;

public interface IncidentService {

    void createIncident(RegisterIncidentDto incidentDto, UserDto user);
    List<Incident>findAllIncidents();
    void assignSecurityInIncident(UUID security, UUID incidentId);
}
