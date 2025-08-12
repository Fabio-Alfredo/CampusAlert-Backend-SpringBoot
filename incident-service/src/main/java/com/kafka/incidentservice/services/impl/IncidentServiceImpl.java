package com.kafka.incidentservice.services.impl;


import com.kafka.incidentservice.Repositories.IncidentRepository;
import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.enums.IncidentStatus;
import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import com.kafka.incidentservice.domain.models.Incident;
import com.kafka.incidentservice.services.contract.IAuthService;
import com.kafka.incidentservice.services.contract.IncidentAuditPublisher;
import com.kafka.incidentservice.services.contract.IncidentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentServiceImpl implements IncidentService {



    private final IncidentRepository incidentRepository;
    private final IAuthService authService;
    private final IncidentAuditPublisher incidentAuditPublisher;


    public IncidentServiceImpl(IncidentRepository incidentRepository, IAuthService authService, IncidentAuditPublisher incidentAuditPublisher) {
        this.incidentRepository = incidentRepository;
        this.authService = authService;
        this.incidentAuditPublisher = incidentAuditPublisher;
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

            incidentAuditPublisher.publishAuditEvent(incident, KafkaEventTypes.REGISTER_INCIDENT, user.getId());
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

    @Override
    public Incident assignSecurityInIncident(UUID security, UUID incidentId) {
        try{

            Incident incident = incidentRepository.findById(incidentId).orElse(null);
            var user = authService.findAuthenticatedUser();
            if(incident == null) {
                throw new RuntimeException("Incident not found");
            }

            if(!authService.isValidUser(security)) {
                throw new RuntimeException("Invalid security user");
            }

            incident.setAssignedTo(security);
            incidentRepository.save(incident);
            incidentAuditPublisher.publishAuditEvent(incident, KafkaEventTypes.ASSIGN_SECURITY, user.getId());
            return incident;
        }catch (Exception e){
            throw new RuntimeException("Error assigning security to incident: " + e.getMessage(), e);
        }
    }

    @Override
    public Incident updateStatus(UUID incidentId, String status) {
        try{
            var incident = incidentRepository.findById(incidentId).orElse(null);
            if(incident == null)
                throw new RuntimeException("Incident not found");

            IncidentStatus validStatus = IncidentStatus.fromString(status);
            incident.setIncidentStatus(validStatus);

            var user = authService.findAuthenticatedUser();
            var updatingIncident = incidentRepository.save(incident);

            incidentAuditPublisher.publishAuditEvent(updatingIncident, KafkaEventTypes.UPDATE_STATUS_INCIDENT, user.getId());

            return updatingIncident;
        }catch (Exception e){
            throw new RuntimeException("Error update status to incident: "+e.getMessage());
        }
    }


}
