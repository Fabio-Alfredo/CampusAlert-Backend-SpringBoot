package com.kafka.auditservice.controllers;

import com.kafka.auditservice.domain.dtos.commons.GeneralResponse;
import com.kafka.auditservice.domain.models.Audit;
import com.kafka.auditservice.services.contract.AuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/audits")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<GeneralResponse> getAllAudits(@RequestParam(required = false) String source_service) {
        try {
            var audits = auditService.getAllAudits(source_service);
            return GeneralResponse.getResponse(HttpStatus.OK, "Audits retrieved successfully", audits);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving audits: " + e.getMessage());
        }
    }
}
