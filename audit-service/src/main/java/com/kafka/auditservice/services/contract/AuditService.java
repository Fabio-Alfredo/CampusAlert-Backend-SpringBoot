package com.kafka.auditservice.services.contract;

import com.kafka.auditservice.domain.dtos.audit.EventAuditDto;
import com.kafka.auditservice.domain.models.Audit;

import java.util.List;

public interface AuditService {
    void createAudit(EventAuditDto userAuditDto);
    List<Audit>getAllAudits( String source_service);
}
