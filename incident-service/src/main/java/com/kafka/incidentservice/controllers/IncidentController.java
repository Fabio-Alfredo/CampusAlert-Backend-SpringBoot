package com.kafka.incidentservice.controllers;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.common.GeneralResponse;
import com.kafka.incidentservice.domain.dtos.incident.RegisterIncidentDto;
import com.kafka.incidentservice.domain.models.Incident;
import com.kafka.incidentservice.services.contract.IAuthService;
import com.kafka.incidentservice.services.contract.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final IAuthService authService;

    public IncidentController(IncidentService incidentService, IAuthService authService) {
        this.incidentService = incidentService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse>finAllIncidents(){
        try{
            List<Incident>incidents = incidentService.findAllIncidents();
            return GeneralResponse.getResponse(HttpStatus.OK, "Incidents fetched successfully", incidents);
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching incidents: " + e.getMessage()+ " Please try again later.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createIncident(@RequestBody @Valid RegisterIncidentDto incidentDto){
        try{
            UserDto user = authService.findAuthenticatedUser();
            incidentService.createIncident(incidentDto, user);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Incident created successfully");
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating incident: " + e.getMessage() + " Please try again later.");
        }
    }

    @PutMapping("/assign-security")
    public ResponseEntity<GeneralResponse>assignSecurityInIncident(@RequestParam("securityId") UUID securityId,
                                                                    @RequestParam("incidentId") UUID incidentId){
        try{

            incidentService.assignSecurityInIncident(securityId, incidentId);
            return GeneralResponse.getResponse(HttpStatus.OK, "Security assigned to incident successfully");
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error assigning security: " + e.getMessage() + " Please try again later.");
        }
    }
}
