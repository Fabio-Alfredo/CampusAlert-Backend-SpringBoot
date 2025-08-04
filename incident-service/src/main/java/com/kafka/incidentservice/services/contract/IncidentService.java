package com.kafka.incidentservice.services.contract;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;

public interface IncidentService {

    void createIncident(RegisterIncidentDto incidentDto, UserDto user);
}
