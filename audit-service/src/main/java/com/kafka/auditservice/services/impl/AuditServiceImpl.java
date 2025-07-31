package com.kafka.auditservice.services.impl;

import com.kafka.auditservice.domain.dtos.audit.UserAuditDto;
import com.kafka.auditservice.domain.models.Audit;
import com.kafka.auditservice.repositories.AuditRepository;
import com.kafka.auditservice.services.contract.AuditService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            newAudit.setSourceService(userAuditDto.getSource_service());

            auditRepository.save(newAudit);
        }catch (Exception e){
            throw new RuntimeException("Error creating audit: " + e.getMessage());
        }
    }

    @Override
    public List<Audit> getAllAudits( String source_service) {
        try{
            List<Audit> audits = new ArrayList<>();
            if(source_service != null && !source_service.isEmpty()) {
                 audits = auditRepository.findAllBySourceService(source_service);
            }else{
                 audits = auditRepository.findAll();
            }

            return audits;
        }catch (Exception e) {
            throw new RuntimeException("Error retrieving audits: " + e.getMessage());
        }
    }
}
