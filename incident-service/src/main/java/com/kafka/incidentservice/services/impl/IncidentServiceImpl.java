package com.kafka.incidentservice.services.impl;

import com.kafka.incidentservice.Repositories.IncidentRepository;
import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.models.Incident;
import com.kafka.incidentservice.services.contract.IncidentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public void createIncident(RegisterIncidentDto incidentDto, UserDto user) {
        try{
            Incident incident = new Incident();
            incident.setTitle(incidentDto.getTitle());
            incident.setDescription(incidentDto.getDescription());
            incident.setIncidentType(incidentDto.getIncidentType());
            incident.setReportedBy(user.getId());

            incidentRepository.save(incident);

        }catch (Exception e) {
            throw new RuntimeException("Error creating incident: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> findAllIncidents() {
        try{
            List<Incident> incidents = incidentRepository.findAll();
            return incidents;
        }catch (Exception e){
            throw new RuntimeException("Error fetching incidents: " + e.getMessage(), e);
        }
    }


}
