package com.kafka.incidentservice.services.impl;

import com.kafka.incidentservice.Repositories.IncidentRepository;
import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.common.KafkaEvents;
import com.kafka.incidentservice.domain.dtos.incident.AuditIncidentDto;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.enums.IncidentStatus;
import com.kafka.incidentservice.domain.enums.KafkaEventTypes;
import com.kafka.incidentservice.domain.models.Incident;
import com.kafka.incidentservice.services.contract.IAuthService;
import com.kafka.incidentservice.services.contract.IncidentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Value("${kafka.topic.incident-and-audit.name}")
    private String incidentTopic;

    private final IncidentRepository incidentRepository;
    private final IAuthService authService;
    private final KafkaTemplate<String, KafkaEvents<AuditIncidentDto>> kafkaTemplate;

    public IncidentServiceImpl(IncidentRepository incidentRepository, IAuthService authService, KafkaTemplate<String, KafkaEvents<AuditIncidentDto>> kafkaTemplate) {
        this.incidentRepository = incidentRepository;
        this.authService = authService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createIncident(RegisterIncidentDto incidentDto, UserDto user) {
        try{
            Incident incident = new Incident();
            incident.setTitle(incidentDto.getTitle());
            incident.setDescription(incidentDto.getDescription());
            incident.setIncidentType(incidentDto.getIncidentType());
            incident.setReportedBy(user.getId());

            var newIncident = incidentRepository.save(incident);
            sendIncidentRegisterAudit(newIncident);
        }catch (Exception e) {
            throw new RuntimeException("Error creating incident: " + e.getMessage(), e);
        }
    }

    private void sendIncidentRegisterAudit(Incident incident){
        AuditIncidentDto auditIncidentDto = new AuditIncidentDto();
        auditIncidentDto.setEventType(KafkaEventTypes.REGISTER_INCIDENT);
        auditIncidentDto.setPayload(incident.getTitle()+" " + incident.getId());
        
        kafkaTemplate.send(incidentTopic, new KafkaEvents<>(KafkaEventTypes.REGISTER_INCIDENT,auditIncidentDto));
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
            if(incident == null) {
                throw new RuntimeException("Incident not found");
            }

            if(!authService.isValidUser(security)) {
                throw new RuntimeException("Invalid security user");
            }

            incident.setAssignedTo(security);
            incidentRepository.save(incident);
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

            return incidentRepository.save(incident);

        }catch (Exception e){
            throw new RuntimeException("Error update status to incident: "+e.getMessage());
        }
    }


}
