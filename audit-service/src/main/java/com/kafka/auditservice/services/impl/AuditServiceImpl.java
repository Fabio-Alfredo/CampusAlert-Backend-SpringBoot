package com.kafka.auditservice.services.impl;

import com.kafka.auditservice.domain.dtos.audit.UserAuditDto;
import com.kafka.auditservice.domain.models.Audit;
import com.kafka.auditservice.repositories.AuditRepository;
import com.kafka.auditservice.services.contract.AuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void createAudit(UserAuditDto userAuditDto) {
        try{
            Audit newAudit = new Audit();
            newAudit.setEventType(userAuditDto.getEventType());
            newAudit.setPayload(userAuditDto.getPayload());
            newAudit.setSource_service(userAuditDto.getSource_service());

            auditRepository.save(newAudit);
        }catch (Exception e){
            throw new RuntimeException("Error creating audit: " + e.getMessage());
        }
    }

    @Override
    public List<Audit> getAllAudits() {
        try{
            var audits = auditRepository.findAll();
            if (audits.isEmpty()) {
                throw new RuntimeException("No audits found");
            }
            return audits;
        }catch (Exception e) {
            throw new RuntimeException("Error retrieving audits: " + e.getMessage());
        }
    }
}
